package com.ataya.contributor.service;

import com.ataya.contributor.dto.store.CartItemStatistics;

public interface KafkaService {
    void suspendItemFromInventory(String itemId, Double quantity);

    void releaseSuspendedInventory(String itemId, Double quantity);

    void releaseSuspendedForSoldItems(CartItemStatistics statistics);
}
