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
public class FireHoseConfig {

    @Value("${data.location}")
    private String dataLocation;

    @Value("${firehose.threadcount}")
    private int threadCount;

    @Value("${firehose.topic}")
    private String topic;

    public FireHoseConfig() {
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public void setDataLocation(String dataLocation) {
        this.dataLocation = dataLocation;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
