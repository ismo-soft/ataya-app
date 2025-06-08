package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.order.ApplyOrderRequest;
import com.ataya.contributor.model.Order;
import com.ataya.contributor.model.Payment;
import com.ataya.contributor.repo.PaymentRepository;
import com.ataya.contributor.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment createPaymentOfOrder(Order order, ApplyOrderRequest request) {
        Payment payment = Payment.builder()
                .orderId(order.getId())
                .paymentMethod(request.getPaymentMethod())
                .transactionId(request.getTransactionId())
                .amount(request.getAmount())
                .status(request.getStatus())
                .note(request.getPaymentNote())
                .paidAt(LocalDateTime.now())
                .isPaid(request.isPaid())
                .build();

        return paymentRepository.save(payment);
    }
}
