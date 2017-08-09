package com.firehose.data;

import com.twitter.bijection.Injection;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
/**
 * Created by rohitkumar on 02/08/17.
 *
 * @implNote This class read data StockPrice data from csv file and
 * send the same to kafka.
 *
 */
public final class StockDataSender implements Runnable {

    private final File csvFile;
    private final KafkaProducer producer;
    private final String kafkaTopic;

    public StockDataSender(File csvFile, KafkaProducer producer, String kafkaTopic) {
        this.csvFile = csvFile;
        this.producer = producer;
        this.kafkaTopic = kafkaTopic;
    }

    private static final String[] STOCK_PRICE_HEADER = {

            "date", "openPrice", "highPrice", "lowPrice",
            "closePrice", "wap", "noOfShares", "noOfTrades", "totalTurnover",
            "deliverableQuantity", "deliQtyToTradedQty", "spreadHighLow", "spreadCloseOpen"
    };

    /**
     *
     * @param csvFile
     * @throws IOException
     */
    public void readFromCsvAndSendToKafka(File csvFile) throws IOException {

        try(CsvBeanReader beanReader = new CsvBeanReader(new FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE)) {

            beanReader.getHeader(false);

            /**
             * TODO Once avro date issue will be fixed then use avroSchemaProducer
             */
            //avroSchemaProducer(beanReader);
            kafkaSeserializerProducer(beanReader);
        }
    }

    private void kafkaSeserializerProducer(CsvBeanReader beanReader) throws IOException {

        StockPrice stockPrice;
        while ((stockPrice = beanReader.read(StockPrice.class, STOCK_PRICE_HEADER, getProcessors())) != null) {
            //System.out.println(stockPrice);
            producer.send(new ProducerRecord<String, StockPrice>(kafkaTopic, stockPrice));
        }
    }

    /**
     * TODO issue while converting Java Date type to Avro schema. As of now fix in as hack in producer
     * but issue in spark consumer while deserializing the values. Please see GenericRecordMapper.
     * @param beanReader
     * @throws IOException
     */
    private void avroSchemaProducer(CsvBeanReader beanReader) throws IOException {

        Injection<GenericRecord, byte[]> stockPriceInjection = GenericRecordMapper.mapClassToRecordInjection(StockPrice.class);
        StockPrice stockPrice;
        while ((stockPrice = beanReader.read(StockPrice.class, STOCK_PRICE_HEADER, getProcessors())) != null) {

            GenericData.Record stockPriceGenericRecord = GenericRecordMapper.mapObjectToRecord(stockPrice);
            byte[] bytes = stockPriceInjection.apply(stockPriceGenericRecord);
            //               System.out.println(stockPrice);
            producer.send(new ProducerRecord<String, byte[]>(kafkaTopic, bytes));
        }
    }

    /**
     *
     * @return
     */
    private CellProcessor[] getProcessors() {

        return new CellProcessor[] {
                new ParseDate("dd-MMM-yy"),
                new ParseDouble(),
                new ParseDouble(),
                new ParseDouble(),
                new ParseDouble(),
                new ParseDouble(),
                new ParseInt(),
                new ParseInt(),
                new ParseDouble(),
                new Optional(new ParseInt()),
                new Optional(new ParseDouble()),
                new Optional(new ParseDouble()),
                new Optional(new ParseDouble())
        };

    }

    @Override
    public void run() {

        try {
            readFromCsvAndSendToKafka(csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
