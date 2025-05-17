package com.ataya.inventory.service.kafka;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CorrelationStorage {
    private final Map<String, TempStorage> correlationStoreMap = new ConcurrentHashMap<>();

    public void store(String correlationId, String storeId, double quantity) {
        correlationStoreMap.put(correlationId, new TempStorage(storeId, quantity));
    }

    public String getStoreId(String correlationId) {
        return correlationStoreMap.get(correlationId).storeId;
    }
    public double getQuantity(String correlationId) {
        return correlationStoreMap.get(correlationId).quantity;
    }

    public void remove(String correlationId) {
        correlationStoreMap.remove(correlationId);
    }

    private record TempStorage(String storeId, double quantity) { }
}