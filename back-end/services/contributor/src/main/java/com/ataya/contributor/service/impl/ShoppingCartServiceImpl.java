package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.shoppingCart.ItemInfoDto;
import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.exception.custom.InvalidOperationException;
import com.ataya.contributor.model.CartItem;
import com.ataya.contributor.model.ShoppingCart;
import com.ataya.contributor.repo.ShoppingCartRepository;
import com.ataya.contributor.service.KafkaService;
import com.ataya.contributor.service.RestService;
import com.ataya.contributor.service.ShoppingCartMovementService;
import com.ataya.contributor.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {


    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMovementService shoppingCartMovementService;
    private final RestService restService;
    private final KafkaService kafkaService;


    @Override
    public String addItemToCart(String userId, String itemId, Double quantity) {
        if (userId == null || userId.isEmpty()) {
            throw new InvalidOperationException(
                    "add items to cart",
                    "User ID cannot be null or empty"
            );
        }
        if (itemId == null || itemId.isEmpty()) {
            throw new InvalidOperationException(
                    "add items to cart",
                    "Items cannot be null or empty"
            );
        }
        if (quantity == null || quantity <= 0 || quantity >= 10) {
            throw new InvalidOperationException(
                    "add items to cart",
                    "Quantity must be greater than 0 and less than 10"
            );
        }
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerId(userId);
        ShoppingCart shoppingCart;
        shoppingCart = optionalCart.orElseGet(() -> ShoppingCart.builder()
                .id(userId)
                .items(List.of())
                .createdAt(LocalDateTime.now())
                .build());

        List<CartItem> items = shoppingCart.getItems();
        System.out.println();
        Optional<CartItem> optionalItem = items.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst();
        if (optionalItem.isPresent()) {
            CartItem existingItem = optionalItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            shoppingCartMovementService.updateItemQuantityMovement(itemId, userId, quantity);

            kafkaService.suspendItemFromInventory(itemId, quantity);
        } else {
            CartItem newItem = restService.getProducts(itemId).get(0);
            if (newItem == null) {
                throw new InvalidOperationException(
                        "add items to cart",
                        "Item with ID: " + itemId + " not found"
                );
            }
            items.add(newItem);
            shoppingCartMovementService.insertNewCartMovement(itemId, userId, quantity);

            kafkaService.suspendItemFromInventory(itemId, quantity);
        }
        shoppingCart.setItems(items);
        shoppingCartRepository.save(shoppingCart);
        return "Item added to cart successfully";
    }

    @Override
    public ShoppingCartDto getCartItems(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new InvalidOperationException(
                    "get cart items",
                    "User ID cannot be null or empty"
            );
        }
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findById(userId);
        ShoppingCart shoppingCart = optionalCart.orElseThrow(() -> new InvalidOperationException(
                "get cart items",
                "No cart found for user ID: " + userId
        ));

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(shoppingCart.getId());
        shoppingCartDto.setCustomerId(shoppingCart.getCustomerId());
        shoppingCartDto.setTotalAmount(shoppingCart.getTotalAmount());
        shoppingCartDto.setItems(shoppingCart.getItems());

        return shoppingCartDto;
    }

    @Override
    public String removeItemFromCart(String userId, Double quantity, String itemId) {
        if (userId == null || userId.isEmpty()) {
            throw new InvalidOperationException(
                    "remove item from cart",
                    "User ID cannot be null or empty"
            );
        }
        if (itemId == null || itemId.isEmpty()) {
            throw new InvalidOperationException(
                    "remove item from cart",
                    "Item ID cannot be null or empty"
            );
        }
        if (quantity == null || quantity <= 0) {
            throw new InvalidOperationException(
                    "remove item from cart",
                    "Quantity must be greater than 0"
            );
        }

        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByCustomerId(userId);
        ShoppingCart shoppingCart = optionalCart.orElseThrow(() -> new InvalidOperationException(
                "remove item from cart",
                "No cart found for user ID: " + userId
        ));

        List<CartItem> items = shoppingCart.getItems();
        Optional<CartItem> optionalItem = items.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst();

        if (optionalItem.isPresent()) {
            CartItem existingItem = optionalItem.get();
            if (existingItem.getQuantity() < quantity) {
                throw new InvalidOperationException(
                        "remove item from cart",
                        "Quantity to remove exceeds the available quantity in the cart"
                );
            }
            existingItem.setQuantity(existingItem.getQuantity() - quantity);
            if (existingItem.getQuantity() <= 0) {
                items.remove(existingItem);
            }
            shoppingCartMovementService.updateItemQuantityMovement(itemId, userId, -quantity);

            kafkaService.releaseSuspendedInventory(itemId, quantity);
        } else {
            throw new InvalidOperationException(
                    "remove item from cart",
                    "Item with ID: " + itemId + " not found in the cart"
            );
        }

        shoppingCart.setItems(items);
        shoppingCartRepository.save(shoppingCart);
        return "Item removed from cart successfully";
    }
}













