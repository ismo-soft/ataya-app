package com.ataya.contributor.service.impl;

import com.ataya.contributor.enums.ShoppingCartMovementType;
import com.ataya.contributor.model.ShoppingCartMovement;
import com.ataya.contributor.repo.ShoppingCartMovementRepository;
import com.ataya.contributor.service.ShoppingCartMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShoppingCartMovementServiceImpl implements ShoppingCartMovementService {

    private final ShoppingCartMovementRepository shoppingCartMovementRepository;

    @Override
    public void insertAddItemMovement(String itemId, String id, Double quantity) {
        ShoppingCartMovement shoppingCart = ShoppingCartMovement.builder()
                .itemId(itemId)
                .shoppingCartId(id)
                .quantity(quantity)
                .movement(ShoppingCartMovementType.ADD)
                .happenedAt(LocalDateTime.now().toString())
                .build();
        shoppingCartMovementRepository.save(shoppingCart);

    }

    @Override
    public void insertNewCartMovement(String itemId, String id, Double quantity) {
        ShoppingCartMovement shoppingCart = ShoppingCartMovement.builder()
                .itemId(itemId)
                .shoppingCartId(id)
                .quantity(quantity)
                .movement(ShoppingCartMovementType.NEW)
                .happenedAt(LocalDateTime.now().toString())
                .build();
        shoppingCartMovementRepository.save(shoppingCart);
        insertAddItemMovement(itemId, id, quantity);
    }

    @Override
    public void updateItemQuantityMovement(String itemId, String userId, Double quantity) {
        ShoppingCartMovement shoppingCart = ShoppingCartMovement.builder()
                .itemId(itemId)
                .shoppingCartId(userId)
                .quantity(quantity)
                .happenedAt(LocalDateTime.now().toString())
                .build();

        if (quantity > 0) {
            shoppingCart.setMovement(ShoppingCartMovementType.INCREASE_QUANTITY);
        } else {
            shoppingCart.setMovement(ShoppingCartMovementType.DECREASE_QUANTITY);
        }

        shoppingCartMovementRepository.save(shoppingCart);
    }

    @Override
    public void deleteCartItemMovement(String itemId, String userId) {
        ShoppingCartMovement shoppingCart = ShoppingCartMovement.builder()
                .itemId(itemId)
                .shoppingCartId(userId)
                .movement(ShoppingCartMovementType.DELETE)
                .happenedAt(LocalDateTime.now().toString())
                .build();
        shoppingCartMovementRepository.save(shoppingCart);
    }


}
