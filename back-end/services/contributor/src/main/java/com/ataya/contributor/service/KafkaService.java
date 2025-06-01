package com.ataya.contributor.service;

public interface KafkaService {
    void suspendItemFromInventory(String itemId, Double quantity);

    void releaseSuspendedInventory(String itemId, Double quantity);
}
