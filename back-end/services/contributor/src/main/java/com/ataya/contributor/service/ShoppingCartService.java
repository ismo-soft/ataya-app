package com.ataya.contributor.service;

import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;

import java.util.Map;

public interface ShoppingCartService {
    String addItemToCart(String userId,String itemId,Double quantity);

    ShoppingCartDto getCartItems(String userId);

    String removeItemFromCart(String userId, Double quantity, String itemId);
}
