package com.ataya.contributor.service;

import com.ataya.contributor.dto.store.CartItemStatistics;
import com.ataya.contributor.enums.ShoppingCartMovementType;
import com.ataya.contributor.model.CartItem;
import com.ataya.contributor.model.ShoppingCartMovement;

import java.util.List;

public interface ShoppingCartMovementService {
    void insertAddItemMovement(String itemId, String id, Double quantity);

    void insertNewCartMovement(String itemId, String id, Double quantity);

    void insertUpdateItemQuantityMovement(String itemId, String userId, Double quantity);

    void insertRemoveCartItemMovement(String itemId, String userId);

    void insertPostedCartItemMovement(String itemId, String id, Double quantity);

    CartItemStatistics reportCartMovements(String id);

    List<ShoppingCartMovement> GetByType(List<ShoppingCartMovement> movements, ShoppingCartMovementType shoppingCartMovementType);

    void removeAllCartMovements(String id);

    void insertResetCartMovement(String id, String id1, List<CartItem> items);
}
