package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.product.ProductItemDtoPage;
import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.dto.store.StoreDto;
import com.ataya.contributor.dto.store.StoreDtoPage;
import com.ataya.contributor.exception.custom.InvalidOperationException;
import com.ataya.contributor.model.ShoppingCart;
import com.ataya.contributor.service.RestService;
import com.ataya.contributor.service.ShoppingCartService;
import com.ataya.contributor.service.ShoppingService;
import com.ataya.contributor.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {

    private final RestService restService;
    private final ShoppingCartService shoppingCartService;


    @Override
    public ApiResponse<List<ProductItemDto>> getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size) {
        if (storeId == null || storeId.isEmpty()) {
            throw new InvalidOperationException(
                    "get products of store",
                    "Store ID cannot be null or empty"
            );
        }
        ProductItemDtoPage productPage = restService.getProducts(storeId, name, category, minPrice, maxPrice, brand, page, size);
        return ApiResponse.<List<ProductItemDto>>builder().
                data(productPage.getProducts())
                .message("Products retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .page(productPage.getPageNumber())
                .size(productPage.getPageSize())
                .total(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<String> addToCart(String userId, String itemId, Double quantity) {
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
        String addItemToCartMessage = shoppingCartService.addItemToCart(userId, itemId, quantity);
        return ApiResponse.<String>builder()
                .data(addItemToCartMessage)
                .message("Items added to cart successfully")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<ShoppingCartDto> getCartItems(String userId) {
        ShoppingCartDto shoppingCartDto = shoppingCartService.getCartItems(userId);
        if (shoppingCartDto == null || shoppingCartDto.getItems().isEmpty()) {
            throw new InvalidOperationException(
                    "get cart items",
                    "No items found in the cart for user ID: " + userId
            );
        }
        return ApiResponse.<ShoppingCartDto>builder()
                .data(shoppingCartDto)
                .message("Cart items retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();

    }

    @Override
    public ApiResponse<String> removeItemFromCart(String userId,Double quantity, String itemId) {
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
        String message = shoppingCartService.removeItemFromCart(userId,quantity ,itemId);
        return ApiResponse.<String>builder()
                .data(message)
                .message("Item removed from cart successfully")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();

    }

    @Override
    public ApiResponse<List<StoreDto>> getAllStores(Integer page, Integer size) {
        StoreDtoPage stores = restService.getAllStores(page, size);
        if (stores.getStores() == null || stores.getStores().isEmpty()) {
            throw new InvalidOperationException(
                    "get all stores",
                    "No stores found"
            );
        }
        return ApiResponse.<List<StoreDto>>builder()
                .data(stores.getStores())
                .message("Stores retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .page(stores.getPageNumber())
                .size(stores.getPageSize())
                .total(stores.getTotalElements())
                .totalPages(stores.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<StoreDto> getStoreById(String storeId) {
        if (storeId == null || storeId.isEmpty()) {
            throw new InvalidOperationException(
                    "get store by ID",
                    "Store ID cannot be null or empty"
            );
        }
        StoreDto store = restService.getStoreById(storeId);
        if (store == null) {
            throw new InvalidOperationException(
                    "get store by ID",
                    "No store found with ID: " + storeId
            );
        }
        return ApiResponse.<StoreDto>builder()
                .data(store)
                .message("Store retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
