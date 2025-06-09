package com.ataya.inventory.service.kafka.consumer;

import com.ataya.inventory.dto.contributor.CartItemStatistics;
import com.ataya.inventory.dto.contributor.SuspendItemRequest;
import com.ataya.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumeSuspendItemRequest {

    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "suspend-item-from-inventory",
            groupId = "inventory-group",
            containerFactory = "suspendItemKafkaListenerContainerFactory"
    )
    public void consume(SuspendItemRequest itemRequest) {
        inventoryService.suspendItem(itemRequest.getItemId(), itemRequest.getQuantity());
    }

    @KafkaListener(
            topics = "release-suspended-inventory",
            groupId = "inventory-group",
            containerFactory = "suspendItemKafkaListenerContainerFactory"
    )
    public void consumeRelease(SuspendItemRequest itemRequest) {
        inventoryService.releaseSuspendedItem(itemRequest.getItemId(), itemRequest.getQuantity());
    }

    @KafkaListener(
            topics = "release-suspended-for-sold-items",
            groupId = "inventory-group",
            containerFactory = "cartItemKafkaListenerContainerFactory"
    )
    public void consumeReleaseForSoldItems(CartItemStatistics itemRequest) {
        inventoryService.releaseSuspendedForSoldItems(itemRequest);
    }
}
