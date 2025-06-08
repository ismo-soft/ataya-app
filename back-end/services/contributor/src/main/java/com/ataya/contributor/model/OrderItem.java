package com.ataya.contributor.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class OrderItem {

    @Id
    private String id;
    private String orderId;
    private String productId;
    private String itemId;
    private Double quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String productName;
}
