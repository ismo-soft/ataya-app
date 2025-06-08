package com.ataya.contributor.service;

import com.ataya.contributor.model.CartItem;
import com.ataya.contributor.model.Order;

import java.util.List;

public interface OrderItemService {
    void createOrderItemsFromCartItems(Order order, List<CartItem> items);
}
