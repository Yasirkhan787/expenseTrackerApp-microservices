package com.yasir.userService.configuration;


import com.yasir.userService.models.UserDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.100.250:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "user-group");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return properties;
    }

    @Bean
    public ConsumerFactory<String, UserDto> consumerFactory() {
        JsonDeserializer<UserDto> deserializer = new JsonDeserializer<>(UserDto.class);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, UserDto>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory(){
//        return new DefaultKafkaConsumerFactory<>(consumerConfig());
//    }
//
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> listenerContainerFactory(){
//        ConcurrentKafkaListenerContainerFactory <String, Object> factory
//                = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }