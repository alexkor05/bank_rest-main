package com.example.email.config;



import com.example.email.dto.NotificationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;


import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, NotificationEvent> kafkaConsumer() {
        Map<String, Object> kafkaConfiguration = new HashMap<>();
        kafkaConfiguration.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaConfiguration.put(ConsumerConfig.GROUP_ID_CONFIG, "email-service-group");
        kafkaConfiguration.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConfiguration.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        kafkaConfiguration.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "*");

        kafkaConfiguration.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, NotificationEvent.class);
        kafkaConfiguration.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);


        return new DefaultKafkaConsumerFactory<>(kafkaConfiguration);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();

        concurrentKafkaListenerContainerFactory.setConsumerFactory(kafkaConsumer());

        return concurrentKafkaListenerContainerFactory;
    }

}
