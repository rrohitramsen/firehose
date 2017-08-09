package com.firehose.kafka;

import com.firehose.config.KafkaConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;
import java.util.Properties;

/**
 * Created by rohitkumar on 03/08/17.
 */
public class Consumers {

    public static KafkaConsumer createKafkaConsumer(KafkaConsumerConfig kafkaConsumerConfig, List<String> topics) {

        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerConfig.getBootstrapServers());
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerConfig.getGroupId());
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerConfig.getEnableAutoCommit());
        consumerProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaConsumerConfig.getAutoCommitIntervalMs());
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfig.getAutoCommitIntervalMs());
        //consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerConfig.getSessionTimeoutMs());
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfig.getKeyDeserializer());
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfig.getValueDeserializer());

        KafkaConsumer kafkaConsumer = new KafkaConsumer(consumerProperties);
        return kafkaConsumer;
    }

}
