package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.store.CartItemStatistics;
import com.ataya.contributor.enums.ShoppingCartMovementType;
import com.ataya.contributor.exception.custom.InvalidOperationException;
import com.ataya.contributor.model.CartItem;
import com.ataya.contributor.model.ShoppingCartMovement;
import com.ataya.contributor.repo.ShoppingCartMovementRepository;
import com.ataya.contributor.service.ShoppingCartMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public CartItemStatistics reportCartMovements(String id) {
        List<ShoppingCartMovement> movements = shoppingCartMovementRepository.findByShoppingCartId(id);
        if (movements.isEmpty()) {
            throw new InvalidOperationException("reportCartMovements", "No movements found for the given shopping cart ID: " + id);
        }
        // represent items all items
        List<ShoppingCartMovement> newMovements = GetByType(movements, ShoppingCartMovementType.NEW);
        // represent items removed from the cart
        List<ShoppingCartMovement> removeMovements = GetByType(movements, ShoppingCartMovementType.REMOVE);
        // represent sold items
        List<ShoppingCartMovement> postMovements = GetByType(movements, ShoppingCartMovementType.POST);
        // all = post quantity - remove quantity
        // new movement: map itemId to count of repetition
        Map<String, Long> newMovementCount = newMovements.stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                ShoppingCartMovement::getItemId,
                                java.util.stream.Collectors.counting()
                        )
                );
        // remove movement: map itemId to count of repetition
        Map<String, Long> removeMovementCount = removeMovements.stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                ShoppingCartMovement::getItemId,
                                java.util.stream.Collectors.counting()
                        )
                );
        // post movement: map itemId to count of repetition
        Map<String, Long> postMovementCount = postMovements.stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                ShoppingCartMovement::getItemId,
                                java.util.stream.Collectors.counting()
                        )
                );

        // post movement: map itemId to total quantity
        Map<String, Double> postMovementTotalQuantity = postMovements.stream()
                .collect(
                        java.util.stream.Collectors.groupingBy(
                                ShoppingCartMovement::getItemId,
                                java.util.stream.Collectors.summingDouble(ShoppingCartMovement::getQuantity)
                        )
                );

        return CartItemStatistics.builder()
                .itemsAddedToCart(newMovementCount)
                .itemsRemovedFromCart(removeMovementCount)
                .itemsSold(postMovementCount)
                .soldQuantity(postMovementTotalQuantity)
                .build();


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

    @Override
    public void removeAllCartMovements(String id) {
        List<ShoppingCartMovement> movements = shoppingCartMovementRepository.findByShoppingCartId(id);
        if (movements.isEmpty()) {
            throw new InvalidOperationException("removeAllCartMovements", "No movements found for the given shopping cart ID: " + id);
        }
        shoppingCartMovementRepository.deleteAll(movements);
    }

    @Override
    public void insertResetCartMovement(String id, String id1, List<CartItem> items) {
        ShoppingCartMovement shoppingCart = ShoppingCartMovement.builder()
                .shoppingCartId(id)
                .movement(ShoppingCartMovementType.RESET)
                .happenedAt(LocalDateTime.now().toString())
                .build();
        shoppingCartMovementRepository.save(shoppingCart);
    }


}
