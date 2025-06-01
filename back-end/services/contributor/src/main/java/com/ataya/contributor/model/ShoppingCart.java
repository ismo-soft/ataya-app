package com.ataya.contributor.model;


import com.ataya.contributor.enums.ShoppingCartStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ShoppingCart {

    @Id
    private String id;
    private String customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
    private ShoppingCartStatus status;
    private Double totalAmount;
    private List<CartItem> items;


}
