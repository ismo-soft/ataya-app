package com.ataya.inventory.service.kafka.consumer;

import com.ataya.inventory.dto.product.ProductDto;
import com.ataya.inventory.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductConsumer {

    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "product-topic", groupId = "inventory-group")
    public void consumeProduct(String jsonMessage) {
        try{
            ProductDto productDto = objectMapper.readValue(jsonMessage, ProductDto.class);
            inventoryService.createProductInventory(productDto);
        } catch (JsonParseException e) {
            throw new RuntimeException("Failed to parse JSON message: " + jsonMessage, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process message: " + jsonMessage, e);
        }
    }
}
