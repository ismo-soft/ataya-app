package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.shoppingCart.ItemInfoDto;
import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.dto.store.CartItemStatistics;
import com.ataya.contributor.enums.ShoppingCartStatus;
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
import java.util.*;

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
        if (quantity == null || quantity <= 0 || quantity > 10) {
            throw new InvalidOperationException(
                    "add items to cart",
                    "Quantity must be greater than 0 and less than 10"
            );
        }

        // get the shopping cart for the user
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByCustomerId(userId);

        // if present, update the cart
        if (shoppingCart.isPresent()) {
            ShoppingCart existingCart = shoppingCart.get();
            List<CartItem> items = existingCart.getItems();

            // Ensure we have a mutable list
            if (items == null) {
                items = new ArrayList<>();
                existingCart.setItems(items);
            }

            Optional<CartItem> optionalItem = items.stream()
                    .filter(item -> item.getItemId().equals(itemId))
                    .findFirst();

            if (optionalItem.isPresent()) {
                CartItem existingItem = optionalItem.get();
                if (existingItem.getQuantity() + quantity > 10) {
                    throw new InvalidOperationException(
                            "add items to cart",
                            "Cannot add more than 10 items of the same type"
                    );
                }
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                Double itemPrice = existingItem.getPrice() * quantity;
                existingCart.setTotalAmount(existingCart.getTotalAmount() + itemPrice);
                shoppingCartMovementService.insertUpdateItemQuantityMovement(itemId, userId, quantity);
                kafkaService.suspendItemFromInventory(itemId, quantity);
            } else {
                CartItem newItem = restService.getProducts(itemId).get(0);
                if (newItem == null) {
                    throw new InvalidOperationException(
                            "add items to cart",
                            "Item with ID: " + itemId + " not found"
                    );
                }
                newItem.setQuantity(quantity);
                Double itemPrice = newItem.getPrice() * quantity;
                existingCart.setTotalAmount(existingCart.getTotalAmount() + itemPrice);
                items.add(newItem);
                shoppingCartMovementService.insertNewCartMovement(itemId, userId, quantity);
                kafkaService.suspendItemFromInventory(itemId, quantity);
            }

            shoppingCartRepository.save(existingCart);
        } else {
            // if not present, create a new cart
            ShoppingCart newCart = ShoppingCart.builder()
                    .id(userId)
                    .customerId(userId)
                    .items(new ArrayList<>())  // Use mutable ArrayList instead of List.of()
                    .createdAt(LocalDateTime.now())
                    .totalAmount(0.0)
                    .status(ShoppingCartStatus.ACTIVE)
                    .build();

            CartItem newItem = restService.getProducts(itemId).get(0);
            if (newItem == null) {
                throw new InvalidOperationException(
                        "add items to cart",
                        "Item with ID: " + itemId + " not found"
                );
            }
            newItem.setQuantity(quantity);
            Double itemPrice = newItem.getPrice() * quantity;
            newCart.setTotalAmount(newCart.getTotalAmount() + itemPrice);

            List<CartItem> items = newCart.getItems();
            items.add(newItem);

            shoppingCartMovementService.insertNewCartMovement(itemId, userId, quantity);
            kafkaService.suspendItemFromInventory(itemId, quantity);
            shoppingCartRepository.save(newCart);
        }

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
            Double itemPrice = existingItem.getPrice() * quantity;
            shoppingCart.setTotalAmount(shoppingCart.getTotalAmount() - itemPrice);
            shoppingCartMovementService.insertUpdateItemQuantityMovement(itemId, userId, -quantity);
            if (existingItem.getQuantity() <= 0) {
                items.remove(existingItem);
                shoppingCartMovementService.insertRemoveCartItemMovement(itemId, userId);
            }
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

    @Override
    public ShoppingCart getCustomerShoppingCart(String id) {
        if (id == null || id.isEmpty()) {
            throw new InvalidOperationException(
                    "get customer shopping cart",
                    "Customer ID cannot be null or empty"
            );
        }
        return shoppingCartRepository.findByCustomerId(id)
                .orElseThrow(() -> new InvalidOperationException(
                        "get customer shopping cart",
                        "No shopping cart found for customer ID: " + id
                ));
    }

    @Override
    public void emptyUserShoppingCart(String id) {
        // Validate the user ID
        if (id == null || id.isEmpty()) {
            throw new InvalidOperationException(
                    "empty user shopping cart",
                    "User ID cannot be null or empty"
            );
        }
        // validate the user have only one shopping cart
        ShoppingCart shoppingCart = shoppingCartRepository.findByCustomerId(id)
                .orElseThrow(() -> new InvalidOperationException(
                        "empty user shopping cart",
                        "No shopping cart found for user ID: " + id
                )
            );
        if (shoppingCart.getItems() == null || shoppingCart.getItems().isEmpty()) {
            throw new InvalidOperationException(
                    "empty user shopping cart",
                    "Shopping cart is already empty for user ID: " + id
            );
        }
        for (CartItem item : shoppingCart.getItems()) {
            shoppingCartMovementService.insertPostedCartItemMovement(
                    item.getItemId(),
                    id,
                    item.getQuantity()
            );
        }

        // create statistics of cart movements then delete them
        CartItemStatistics statistics = shoppingCartMovementService.reportCartMovements(shoppingCart.getId());

        // map string to double for itemId to item price
        Map<String, Double> itemPriceMap = new HashMap<>();
        for (CartItem item : shoppingCart.getItems()) {
            itemPriceMap.put(item.getItemId(), item.getPrice());
        }
        // map string to double for itemId to total price
        Map<String, Double> itemTotalPriceMap = new HashMap<>();
        for (CartItem item : shoppingCart.getItems()) {
            itemTotalPriceMap.put(item.getItemId(), item.getPrice() * item.getQuantity());
        }
        statistics.setSoldPrice(itemPriceMap);
        statistics.setTotalPriceOfSoldItems(itemTotalPriceMap);

        shoppingCartMovementService.removeAllCartMovements(shoppingCart.getId());
        // delete the shopping cart
        shoppingCartRepository.delete(shoppingCart);
        // kafka message to remove suspended items from inventory, with items statistics
        kafkaService.releaseSuspendedForSoldItems(statistics);
    }

    @Override
    public Map<String, Boolean> checkItemsAvailability(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidOperationException(
                    "check items availability",
                    "Items list cannot be null or empty"
            );
        }
        // create a map of itemId to quantity
        Map<String, Double> itemToQuantityMap = new HashMap<>();
        for (CartItem item : items) {
            if (item.getItemId() == null || item.getItemId().isEmpty()) {
                throw new InvalidOperationException(
                        "check items availability",
                        "Item ID cannot be null or empty"
                );
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new InvalidOperationException(
                        "check items availability",
                        "Item quantity must be greater than 0"
                );
            }
            itemToQuantityMap.put(item.getItemId(), item.getQuantity());
        }
        return restService.areItemsAvailableToBuy(itemToQuantityMap);
    }
}



