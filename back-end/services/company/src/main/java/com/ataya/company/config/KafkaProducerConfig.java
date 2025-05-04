package com.ataya.company.config;

import com.ataya.company.dto.product.ProductDto;
import com.ataya.company.dto.store.StoreDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    // Common Kafka configuration map
    private Map<String, Object> getCommonConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return config;
    }

    // Kafka producer configuration for sending ProductDto messages
    @Bean
    public ProducerFactory<String, ProductDto> productProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonConfig());
    }

    @Bean
    public KafkaTemplate<String, ProductDto> productKafkaTemplate() {
        return new KafkaTemplate<>(productProducerFactory());
    }

    // Kafka producer configuration for sending StoreDto messages
    @Bean
    public ProducerFactory<String, StoreDto> storeProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getCommonConfig());
    }

    @Bean
    public KafkaTemplate<String, StoreDto> storeKafkaTemplate() {
        return new KafkaTemplate<>(storeProducerFactory());
    }
}