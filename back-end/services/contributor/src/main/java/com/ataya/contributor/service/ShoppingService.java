package com.ataya.contributor.service;

import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.dto.store.StoreDto;
import com.ataya.contributor.util.ApiResponse;

import java.util.List;
import java.util.Map;

public interface ShoppingService {
    ApiResponse<List<ProductItemDto>> getProducts(String storeId, String name,String category,Double minPrice,Double maxPrice, String brand, int page, int size);

    ApiResponse<String> addToCart(String userId, String itemId, Double quantity);

    ApiResponse<ShoppingCartDto> getCartItems(String userId);

    ApiResponse<String> removeItemFromCart(String id, Double quantity, String itemId);

    ApiResponse<List<StoreDto>> getAllStores(Integer page, Integer size);

    ApiResponse<StoreDto> getStoreById(String storeId);
}
