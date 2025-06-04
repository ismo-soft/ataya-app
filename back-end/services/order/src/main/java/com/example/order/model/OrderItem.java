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
public class OrderItem {
    @Id
    private String id;
    private String itemId;
    private String productId;
    private String productName;
    private Double price;
    private Double quantity;
    private String imageUrl;
}
