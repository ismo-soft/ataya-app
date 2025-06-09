package com.ataya.contributor.service;

import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.model.CartItem;
import com.ataya.contributor.model.ShoppingCart;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    String addItemToCart(String userId,String itemId,Double quantity);

    ShoppingCartDto getCartItems(String userId);

    String removeItemFromCart(String userId, Double quantity, String itemId);

    ShoppingCart getCustomerShoppingCart(String id);

    void emptyUserShoppingCart(String id);

    Map<String, Boolean> checkItemsAvailability(List<CartItem> items);
}
