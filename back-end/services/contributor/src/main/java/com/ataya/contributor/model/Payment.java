package com.ataya.contributor.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Payment {
    @Id
    private String id;
    private String orderId;
    private String paymentMethod;
    private String transactionId;
    private Double amount;
    private String status;
    private String note;
    private LocalDateTime paidAt;
    private Boolean isPaid;
}
