package com.ataya.company.service.kafka.producer;

import com.ataya.company.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductProducer {

    private final KafkaTemplate<String, ProductDto> kafkaTemplate;

    public void sendProduct(ProductDto productDto) {
        kafkaTemplate.send("product-topic", productDto);
    }

}
