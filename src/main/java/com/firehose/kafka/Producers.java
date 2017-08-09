package com.firehose.kafka;

import com.firehose.config.KafkaProducerConfig;
import com.firehose.data.StockPriceSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by rohitkumar on 03/08/17.
 */
public class Producers {

    /**
     *
     * @param kafkaProducerConfig
     * @return KafkaProducer
     */
    public static KafkaProducer createKafkaProducer(KafkaProducerConfig kafkaProducerConfig) {

        Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerConfig.getBootstrapServers());
        producerProperties.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfig.getAcks());
        producerProperties.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfig.getRetries());
        producerProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfig.getBatchSize());
        producerProperties.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfig.getLingerMs());
        producerProperties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProducerConfig.getBufferMemory());
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getKeySerializer());
       //producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerConfig.getValueSerializer());
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StockPriceSerializer.class);
        setupBatchingAndCompression(producerProperties);
        KafkaProducer kafkaProducer = new KafkaProducer(producerProperties);
        return kafkaProducer;
    }

    /**
     *
     * @param props
     */
    private static void setupBatchingAndCompression(final Properties props) {

        //props.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,  16384 * 4);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
    }

    /**
     *
     * @param props
     */
    private static void setupRetriesInFlightTimeout(Properties props) {

        //Only two in-flight messages per Kafka broker connection
        // - max.in.flight.requests.per.connection (default 5)
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,
                1);
        //Set the number of retries - retries
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        //Request timeout - request.timeout.ms
        //props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 15_000);

        //Only retry after one second.
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1_000);
    }

}
