package com.example.order.model;


import com.example.order.enums.OrderStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Order {
    @Id
    private String id;
    private String shoppingCartId;
    private Double totalAmount;
    private OrderStatus status;
    private String buyerId;

}
