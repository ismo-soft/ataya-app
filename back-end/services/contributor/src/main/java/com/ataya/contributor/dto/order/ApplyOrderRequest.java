package com.ataya.contributor.dto.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyOrderRequest {
    private String orderNote;
    private String paymentMethod;
    private String transactionId;
    private Double amount;
    private String status;
    private String paymentNote;
    private boolean isPaid;
}
