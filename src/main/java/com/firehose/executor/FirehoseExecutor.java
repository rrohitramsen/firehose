package com.firehose.executor;

import com.datastax.spark.connector.japi.CassandraJavaUtil;
import com.firehose.config.FireHoseConfig;
import com.firehose.config.KafkaConsumerConfig;
import com.firehose.config.KafkaProducerConfig;
import com.firehose.data.*;
import com.firehose.kafka.Producers;
import com.firehose.utils.FileUtils;
import kafka.serializer.StringDecoder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import org.apache.spark.streaming.kafka.KafkaUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rohitkumar on 03/08/17.
 */
public class FireHoseExecutor {

    private final FireHoseConfig fireHoseConfig;
    private final KafkaConsumerConfig kafkaConsumerConfig;
    private final KafkaProducerConfig kafkaProducerConfig;

    public FireHoseExecutor(FireHoseConfig fireHoseConfig, KafkaConsumerConfig kafkaConsumerConfig, KafkaProducerConfig kafkaProducerConfig) {
        this.fireHoseConfig = fireHoseConfig;
        this.kafkaConsumerConfig = kafkaConsumerConfig;
        this.kafkaProducerConfig = kafkaProducerConfig;
    }

    /**
     * Starts fire-hose producer.
     */
    public void executeProducer() {

        ExecutorService executorService = Executors.newFixedThreadPool(fireHoseConfig.getThreadCount());

        List<File> files = new ArrayList<>();
        FileUtils.listfiles(fireHoseConfig.getDataLocation(), files);

        KafkaProducer<String, StockPrice> producer = Producers.createKafkaProducer(kafkaProducerConfig);

        for (File file : files) {

            StockDataSender stockDataSender = new StockDataSender(file, producer, fireHoseConfig.getTopic());
            executorService.submit(stockDataSender);
        }
    }

    /**
     * TODO avro date issue while deserializing. So not using avro for now.
     */
    public void executeAvroConsumer() throws InterruptedException {

        /*SparkConf conf = new SparkConf()
                .setAppName("kafka-firehose")
                .setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sc, new Duration(2000));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", kafkaConsumerConfig.getBootstrapServers());
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", BytesDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);
        kafkaParams.put("metadata.broker.list", kafkaConsumerConfig.getBootstrapServers());

        Collection<String> topics = Arrays.asList(fireHoseConfig.getTopic());

        JavaInputDStream<ConsumerRecord<String, Bytes>> directKafkaStream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, Bytes>Subscribe(topics, kafkaParams)
                );

        directKafkaStream.foreachRDD(rdd -> {
            rdd.foreach(avroRecord -> {
                Injection<GenericRecord, byte[]> recordInjection = GenericRecordMapper.mapClassToRecordInjection(StockPrice.class);
                GenericRecord record = recordInjection.invert(avroRecord.value().get()).get();
                StockPrice stockPrice = new StockPrice();
                stockPrice = GenericRecordMapper.mapRecordToObject(record, stockPrice);
            });
        });

        streamingContext.start();
        streamingContext.awaitTermination();*/
    }

    /**
     * spark-streaming-kafka-0-10_2.11 not working
     * TODO As of now kafka-0-10 is not stable with spark streaming. Its not working with multiple brokers.
     * That's why shifting to kafka-0-8
     */
    public void executeConsumer() throws InterruptedException {

        /*SparkConf conf = new SparkConf()
                .setAppName("kafka-firehose")
                .setMaster("local[*]");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkContext, new Duration(2000));

        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", kafkaConsumerConfig.getBootstrapServers());
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("value.deserializer", StockPriceDeserializer.class);
        kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", false);

        Collection<String> topics = Arrays.asList(fireHoseConfig.getTopic());

        final JavaInputDStream<ConsumerRecord<String, StockPrice>> directKafkaStream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        LocationStrategies.PreferConsistent(),
                        ConsumerStrategies.<String, StockPrice>Subscribe(topics, kafkaParams)
                );

        directKafkaStream.mapToPair(record -> new Tuple2<>(record.key(), record.value())).foreachRDD(new VoidFunction2<JavaPairRDD<String, StockPrice>, Time>() {
            @Override
            public void call(JavaPairRDD<String, StockPrice> rdd, Time v2) throws Exception {
                rdd.foreach(record -> System.out.println("**** Spark Consumer ****"+record._2()));
            }
        });

        streamingContext.start();
        streamingContext.awaitTermination();*/
    }


    /**
     * spark-streaming-kafka-0-10_2.11 not working
     * TODO As of now kafka-0-10 is not stable with spark streaming. Thats why shifting to kafka-0-8
     */
    public void executeConsumerKafka_0_8() throws InterruptedException {

        SparkConf conf = new SparkConf()
                .setAppName("kafka-firehose")
                .setMaster("local[*]")
                /**
                 * Please see https://stackoverflow.com/questions/23528006/how-jobs-are-assigned-to-executors-in-spark-streaming
                 *
                 * Actually, in the current implementation of Spark Streaming and under default configuration, only job is active (i.e. under execution) at any point of time.
                 * So if one batch's processing takes longer than 10 seconds, then then next batch's jobs will stay queued.
                 * This can be changed with an experimental Spark property "spark.streaming.concurrentJobs" which is by default
                 * set to 1. Its not currently documented (maybe I should add it).
                 * The reason it is set to 1 is that concurrent jobs can potentially lead to weird sharing of resources
                 * and which can make it hard to debug the whether there is sufficient resources in the system to
                 * process the ingested data fast enough. With only 1 job running at a time,
                 * it is easy to see that if batch processing time < batch interval,
                 * then the system will be stable. Granted that this may not be the most efficient use of resources
                 * under certain conditions. We definitely hope to improve this in the future.
                 *
                 * "spark.streaming.concurrentJobs","1" - Scheduling Delay around - 3.43 second and Processing Time - 9.8 Seconds
                 * "spark.streaming.concurrentJobs","2 - Improved - Scheduling Delay - 1 milli-second - 8.8 seconds
                 * "spark.streaming.concurrentJobs","4" - Scheduling Delay - 2 milli-second - 8.8 seconds.
                 *
                 * Rest of the stats has not seen much impact.
                 *
                 */
                .set("spark.streaming.concurrentJobs","2");
                /**
                 * Spark gives a very powerful feature called backpressure .
                 * Having this property enabled means spark streaming will tell kafka to slow down rate of sending messages
                 * if the processing time is coming more than batch interval and scheduling delay is increasing.
                 * Its helpful in cases like when there is sudden surge in data flow and is a must have property to have in production to avoid application being over burdened.
                 * However this property should be disabled during development and staging phase otherwise we cannot test the limit of the maximum load our application can and should handle.
                 * set(spark.streaming.backpressure.enabled","true");
                 */

        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkContext, new Duration(15000));

        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", kafkaConsumerConfig.getBootstrapServers());
        kafkaParams.put("zookeeper.connect", kafkaConsumerConfig.getZookeeperConnect());
        kafkaParams.put("group.id", "test");

        Set<String> topics = new HashSet<>();
        topics.add(fireHoseConfig.getTopic());

        JavaPairInputDStream<String, StockPrice> directKafkaStream =
                KafkaUtils.createDirectStream(
                        streamingContext,
                        String.class,
                        StockPrice.class,
                        StringDecoder.class,
                        StockPriceDecoder.class,
                        kafkaParams,
                        topics
                );


        directKafkaStream.foreachRDD((rdd, v2) -> {
            rdd.foreach(stringStockPriceTuple2 -> stringStockPriceTuple2._2());
        });


        //saveToCassandra(directKafkaStream);

        streamingContext.start();
        streamingContext.awaitTermination();

    }

    /**
     * Save JavaRDD<StockPrice> in cassandra.
     * @param directKafkaStream
     */
    private void saveToCassandra(JavaPairInputDStream<String, StockPrice> directKafkaStream) {

        Map<String, String> columnNameMappings = new HashMap<>();

        directKafkaStream.foreachRDD((rdd, v2) -> {
            /**
             * Convert JavaPairRDD<String, StockPrice> into JavaRDD<StockPrice>.
             * Then Saving an RDD of StockPrice objects to a Cassandra Table
             */
            CassandraJavaUtil.javaFunctions(rdd.map(stockPriceTuple -> stockPriceTuple._2())
                    .rdd())
                    .writerBuilder(
                            "ks", "stockPrice",
                            CassandraJavaUtil.mapToRow(StockPrice.class, columnNameMappings)
                    ).saveToCassandra();

        });
    }


}
