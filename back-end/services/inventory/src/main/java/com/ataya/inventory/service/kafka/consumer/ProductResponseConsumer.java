package com.ataya.inventory.service.kafka.consumer;


import com.ataya.inventory.dto.company.ProductDto;
import com.ataya.inventory.service.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductResponseConsumer {

    private final ObjectMapper objectMapper;
    private final InventoryService inventoryService;

    @KafkaListener(topics = "product-details-response", groupId = "inventory-group")
    public void consume(String jsonMessage) throws JsonProcessingException {
        // Deserialize the JSON message into a ProductDto object
        ProductDto productDto = objectMapper.readValue(jsonMessage, ProductDto.class);

        // Call the createInventoryFromProductDto method with the deserialized object
        inventoryService.createInventoryFromProductDto(productDto);

    }
}
