package com.firehose.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by rohitkumar on 03/08/17.
 */
public class StockPriceDeserializer implements Deserializer<StockPrice> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public StockPrice deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();
        StockPrice stockPrice = null;
        try {
            stockPrice = objectMapper.readValue(data, StockPrice.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockPrice;
    }

    @Override
    public void close() {

    }
}
