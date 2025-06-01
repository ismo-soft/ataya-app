package com.ataya.contributor.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    private String itemId;
    private String productId;
    private String productName;
    private Double price;
    private Double quantity;
    private String imageUrl;
}