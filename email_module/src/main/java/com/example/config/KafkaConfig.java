package com.example.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        Map<String, Object> kafkaConfiguration = new HashMap<>();
        kafkaConfiguration.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfiguration.put(ConsumerConfig.GROUP_ID_CONFIG, "spring-kafka-config");

        return null;

    }

}
