package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.order.ApplyOrderRequest;
import com.ataya.contributor.enums.OrderStatus;
import com.ataya.contributor.enums.ShoppingCartStatus;
import com.ataya.contributor.exception.custom.InvalidOperationException;
import com.ataya.contributor.exception.custom.ResourceNotFoundException;
import com.ataya.contributor.model.Contributor;
import com.ataya.contributor.model.Order;
import com.ataya.contributor.model.Payment;
import com.ataya.contributor.model.ShoppingCart;
import com.ataya.contributor.repo.OrderRepository;
import com.ataya.contributor.service.*;
import com.ataya.contributor.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ContributorService contributorService;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemService orderItemService;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public ApiResponse<String> applyOrder(ApplyOrderRequest request, Contributor user) {

        Contributor contributor = contributorService.getContributorById(user.getId());
        if (contributor == null) {
            throw new ResourceNotFoundException("Contributor", "user.id", user.getId());
        }

        ShoppingCart shoppingCart = shoppingCartService.getCustomerShoppingCart(user.getId());
        if (shoppingCart == null) {
            throw new ResourceNotFoundException("ShoppingCart", "user.id", user.getId());
        }
        if (shoppingCart.getItems().isEmpty()) {
            throw new InvalidOperationException("apply Order", "Shopping cart is empty");
        }
        Map<String,Boolean> itemAvailability = shoppingCartService.checkItemsAvailability(shoppingCart.getItems());
        Map<String, Boolean> outOfStockItems = new HashMap<>();
        itemAvailability.forEach((itemId, isAvailable) -> {
            if (!isAvailable) {
                outOfStockItems.put(itemId, false);
            }
        });
        if (!outOfStockItems.isEmpty()) {
            throw new InvalidOperationException("apply Order", "Some items are out of stock: " + outOfStockItems.keySet());
        }

        Optional<Order> orderOptional = orderRepository.findByBuyerIdAndStatus(user.getId(), OrderStatus.PENDING);
        Order order;
        if (orderOptional.isPresent()) {
            order = orderOptional.get();
        } else {
            order = Order.builder()
                    .buyerId(contributor.getId())
                    .totalAmount(shoppingCart.getTotalAmount())
                    .note(request.getOrderNote())
                    .status(OrderStatus.PENDING)
                    .build();
            order = orderRepository.save(order);
        }
        Payment payment = paymentService.createPaymentOfOrder(order, request);
        if (payment == null) {
            throw new InvalidOperationException("apply Order", "Payment creation failed");
        }
        if (!Objects.equals(payment.getAmount(), order.getTotalAmount())) {
            throw new InvalidOperationException("apply Order", "Payment amount does not match order total");
        }
        if (payment.getIsPaid()!= null && payment.getIsPaid()) {
            order.setStatus(OrderStatus.PAID);
            order.setPaidAt(payment.getPaidAt());
            order.setPaymentId(payment.getId());
            orderItemService.createOrderItemsFromCartItems(order, shoppingCart.getItems());
        } else {
            order.setStatus(OrderStatus.PENDING);
        }
        if (order.getStatus() == OrderStatus.PENDING) {
            throw new InvalidOperationException("apply Order", "Order status still PENDING");
        }
        order = orderRepository.save(order);
        if (order.getStatus() == OrderStatus.PAID) {
            shoppingCartService.emptyUserShoppingCart(user.getId());
        }
        return ApiResponse.<String>builder()
                .message("Order applied successfully")
                .data(order.getId())
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();

    }
}
