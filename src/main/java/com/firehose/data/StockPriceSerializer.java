package com.firehose.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by rohitkumar on 03/08/17.
 */
public class StockPriceSerializer implements Serializer<StockPrice> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, StockPrice data) {

        ObjectMapper objectMapper = new ObjectMapper();
        byte[] byteArray = new byte[0];
        try {
            byteArray = objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    @Override
    public void close() {

    }
}
