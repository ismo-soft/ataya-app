package com.ataya.contributor.service;

import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.util.ApiResponse;

import java.util.List;

public interface ShoppingService {
    ApiResponse<List<ProductItemDto>> getProducts(String storeId);

    ApiResponse<ShoppingCartDto> addToCart(String id, List<String> items);

    ApiResponse<ShoppingCartDto> getCartItems(String id);

    ApiResponse<ShoppingCartDto> removeItemFromCart(String id, String itemId);
}
