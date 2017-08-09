package com.firehose.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * Created by rohitkumar on 03/08/17.
 *
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class KafkaProducerConfig {

    @Value("${producer.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${producer.acks}")
    private String acks;

    @Value("${producer.retries}")
    private String retries;

    @Value("${producer.batch.size}")
    private String batchSize;

    @Value("${producer.linger.ms}")
    private String lingerMs;

    @Value("${producer.buffer.memory}")
    private String bufferMemory;

    @Value("${producer.key.serializer}")
    private String keySerializer;

    @Value("${producer.value.serializer}")
    private String valueSerializer;

    public KafkaProducerConfig() {
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getLingerMs() {
        return lingerMs;
    }

    public void setLingerMs(String lingerMs) {
        this.lingerMs = lingerMs;
    }

    public String getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(String bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

}
