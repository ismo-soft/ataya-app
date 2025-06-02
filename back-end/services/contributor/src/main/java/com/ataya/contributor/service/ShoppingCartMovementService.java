package com.ataya.contributor.service;

public interface ShoppingCartMovementService {
    void insertAddItemMovement(String itemId, String id, Double quantity);

    void insertNewCartMovement(String itemId, String id, Double quantity);

    void updateItemQuantityMovement(String itemId, String userId, Double quantity);

    void deleteCartItemMovement(String itemId, String userId);
}
