package com.firehose.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * Created by rohitkumar on 03/08/17.
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class KafkaConsumerConfig {

    @Value("${consumer.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${consumer.group.id}")
    private String groupId;

    @Value("${consumer.enable.auto.commit}")
    private String enableAutoCommit;

    @Value("${consumer.auto.commit.interval.ms}")
    private String autoCommitIntervalMs;

    @Value("${consumer.auto.offset.reset}")
    private String autoOffsetReset;

    @Value("${consumer.session.timeout.ms}")
    private String sessionTimeoutMs;

    @Value("${consumer.key.deserializer}")
    private String keyDeserializer;

    @Value("${consumer.value.deserializer}")
    private String valueDeserializer;

    @Value("${consumer.metadata.broker.list}")
    private String metadataBrokerList;

    @Value("${consumer.zookeeper.connect}")
    private String zookeeperConnect;

    public KafkaConsumerConfig() {
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(String enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public String getAutoCommitIntervalMs() {
        return autoCommitIntervalMs;
    }

    public void setAutoCommitIntervalMs(String autoCommitIntervalMs) {
        this.autoCommitIntervalMs = autoCommitIntervalMs;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(String sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    public String getMetadataBrokerList() {
        return metadataBrokerList;
    }

    public void setMetadataBrokerList(String metadataBrokerList) {
        this.metadataBrokerList = metadataBrokerList;
    }

    public String getZookeeperConnect() {
        return zookeeperConnect;
    }

    public void setZookeeperConnect(String zookeeperConnect) {
        this.zookeeperConnect = zookeeperConnect;
    }
}
