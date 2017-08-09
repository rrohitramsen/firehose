package com.firehose.config;

import com.firehose.executor.FireHoseExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Created by rohitkumar on 03/08/17.
 */
@SpringBootApplication
public class FireHose implements CommandLineRunner {

    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;

    @Autowired
    private FireHoseConfig fireHoseConfig;

    public static void main(String[] args) {

        SpringApplication springApplication = new SpringApplication(FireHose.class);
        springApplication.setWebEnvironment(false);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);

    }

    /**
     *
     * @param strings
     * @throws Exception
     */
    public void run(String... strings) throws Exception {

        FireHoseExecutor fireHoseExecutor = new FireHoseExecutor(fireHoseConfig, kafkaConsumerConfig, kafkaProducerConfig);
        fireHoseExecutor.executeProducer();

        Thread.currentThread().sleep(3000);

        fireHoseExecutor.executeConsumerKafka_0_8();

    }
}
