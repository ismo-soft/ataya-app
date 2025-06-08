package com.ataya.contributor.service.impl;

import com.ataya.contributor.model.CartItem;
import com.ataya.contributor.model.Order;
import com.ataya.contributor.model.OrderItem;
import com.ataya.contributor.repo.OrderItemRepository;
import com.ataya.contributor.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    @Override
    public void createOrderItemsFromCartItems(Order order, List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return; // No items to process
        }
        List<OrderItem> toSaveOrderItems = new ArrayList<>();
        items.forEach(
                item -> {
                    OrderItem orderItem = OrderItem.builder()
                            .orderId(order.getId())
                            .itemId(item.getItemId())
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getPrice())
                            .totalPrice(item.getPrice() * item.getQuantity())
                            .productName(item.getProductName())
                            .build();
                    toSaveOrderItems.add(orderItem);
                }
        );
        if (!toSaveOrderItems.isEmpty()) {
            orderItemRepository.saveAll(toSaveOrderItems);
        }
    }
}
