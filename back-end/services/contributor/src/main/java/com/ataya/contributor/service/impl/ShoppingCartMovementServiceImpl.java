package com.ataya.contributor.service.impl;

import com.ataya.contributor.enums.ShoppingCartMovementType;
import com.ataya.contributor.model.ShoppingCartMovement;
import com.ataya.contributor.repo.ShoppingCartMovementRepository;
import com.ataya.contributor.service.ShoppingCartMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .movement(ShoppingCartMovementType.NEW)
                .happenedAt(LocalDateTime.now().toString())
                .build();
        shoppingCartMovementRepository.save(shoppingCart);
        insertAddItemMovement(itemId, id, quantity);
    }

    @Override
    public void insertUpdateItemQuantityMovement(String itemId, String userId, Double quantity) {
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
    public void insertRemoveCartItemMovement(String itemId, String userId) {
        ShoppingCartMovement shoppingCart = ShoppingCartMovement.builder()
                .itemId(itemId)
                .shoppingCartId(userId)
                .movement(ShoppingCartMovementType.REMOVE)
                .happenedAt(LocalDateTime.now().toString())
                .build();
        shoppingCartMovementRepository.save(shoppingCart);
    }

    @Override
    public void insertPostedCartItemMovement(String itemId, String id, Double quantity) {
        ShoppingCartMovement shoppingCart = ShoppingCartMovement.builder()
                .itemId(itemId)
                .shoppingCartId(id)
                .quantity(quantity)
                .movement(ShoppingCartMovementType.POST)
                .happenedAt(LocalDateTime.now().toString())
                .build();
        shoppingCartMovementRepository.save(shoppingCart);
    }

    @Override
    public Object reportCartMovements(String id) {
        List<ShoppingCartMovement> movements = shoppingCartMovementRepository.findByShoppingCartId(id);
        if (movements.isEmpty()) {
            return "No movements found for the shopping cart with ID: " + id;
        }

        List<ShoppingCartMovement> newMovements = GetByType(movements, ShoppingCartMovementType.NEW);
        List<ShoppingCartMovement> addMovements = GetByType(movements, ShoppingCartMovementType.ADD);
        List<ShoppingCartMovement> removeMovements = GetByType(movements, ShoppingCartMovementType.REMOVE);
        List<ShoppingCartMovement> increaseMovements = GetByType(movements, ShoppingCartMovementType.INCREASE_QUANTITY);
        List<ShoppingCartMovement> decreaseMovements = GetByType(movements, ShoppingCartMovementType.DECREASE_QUANTITY);
        List<ShoppingCartMovement> postMovements = GetByType(movements, ShoppingCartMovementType.POST);
        return null;
    }

    public List<ShoppingCartMovement> GetByType(List<ShoppingCartMovement> movements, ShoppingCartMovementType shoppingCartMovementType) {
        List<ShoppingCartMovement> filteredMovements = new ArrayList<>();
        for (ShoppingCartMovement movement : movements) {
            if (movement.getMovement() == shoppingCartMovementType) {
                filteredMovements.add(movement);
            }
        }
        return filteredMovements;
    }


}
