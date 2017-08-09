package com.firehose.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.serializer.Decoder;
import kafka.utils.VerifiableProperties;

import java.io.IOException;

/**
 * Created by rohitkumar on 05/08/17.
 */
public class StockPriceDecoder implements Decoder<StockPrice> {

    /**
     * Have to add this constructor because kafka 0_8_2
     * @param verifiableProperties
     */
    public StockPriceDecoder(VerifiableProperties verifiableProperties) {
    }

    public StockPriceDecoder() {
    }

    @Override
    public StockPrice fromBytes(byte[] bytes) {

        ObjectMapper objectMapper = new ObjectMapper();
        StockPrice stockPrice = null;
        try {
            stockPrice = objectMapper.readValue(bytes, StockPrice.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stockPrice;
    }
}
