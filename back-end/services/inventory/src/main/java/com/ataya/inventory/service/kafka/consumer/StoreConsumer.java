package com.ataya.inventory.service.kafka.consumer;

import com.ataya.inventory.dto.company.StoreDto;
import com.ataya.inventory.repo.InventoryRepository;
import com.ataya.inventory.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreConsumer {
    private final InventoryService inventoryService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "store-topic", groupId = "inventory-group")
    public void consumeStore(String jsonMessage) {
        try {
            StoreDto storeDto = objectMapper.readValue(jsonMessage, StoreDto.class);
            inventoryService.createStoreInventory(storeDto);
        } catch (JsonParseException e) {
            throw new RuntimeException("Failed to parse JSON message: " + jsonMessage, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process message: " + jsonMessage, e);
        }
    }
}
