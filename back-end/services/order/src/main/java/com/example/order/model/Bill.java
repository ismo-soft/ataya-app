package com.example.order.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Bill {
    @Id
    private String id;
    private String orderId;
    private String buyerId;
    private String paymentMethod;
    private Double paidAmount;


}
