package com.ataya.contributor.model;

import com.ataya.contributor.enums.OrderStatus;
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
public class Order {
    @Id
    private String id;
    private String buyerId;
    private Double totalAmount;
    private OrderStatus status;
    private String note;
    private LocalDateTime paidAt;
    private String paymentId;
}
