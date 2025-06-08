package com.ataya.contributor.service;

import com.ataya.contributor.dto.order.ApplyOrderRequest;
import com.ataya.contributor.model.Order;
import com.ataya.contributor.model.Payment;

public interface PaymentService {
    Payment createPaymentOfOrder(Order order, ApplyOrderRequest request);
}
