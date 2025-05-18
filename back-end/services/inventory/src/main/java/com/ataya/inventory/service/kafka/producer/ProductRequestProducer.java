package com.ataya.inventory.service.kafka.producer;

import com.ataya.inventory.dto.company.ProductInfoRequestDto;
import com.ataya.inventory.service.kafka.CorrelationStorage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductRequestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final CorrelationStorage correlationStorage;

    public void requestProductDetails(String storeId, String productId, double quantity, String Username) {
        System.out.println("Hi, I am in requestProductDetails method");
        String correlationId = UUID.randomUUID().toString();
        correlationStorage.store(correlationId, storeId, quantity);

        ProductInfoRequestDto request = new ProductInfoRequestDto();
        request.setCorrelationId(correlationId);
        request.setStoreId(storeId);
        request.setProductId(productId);
        request.setQuantity(quantity);
        request.setUsername(Username);

        try {
            String jsonMessage = objectMapper.writeValueAsString(request);
            kafkaTemplate.send("product-details-request", correlationId, jsonMessage);
        } catch (JsonProcessingException e) {
            // Handle the exception (e.g., log it)
            throw new RuntimeException("Failed to send product info request",e);
        }

    }
}
