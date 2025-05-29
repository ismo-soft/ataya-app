package com.ataya.contributor.dto.shoppingCart;


import com.ataya.contributor.enums.ShoppingCartStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartDto {
    private String id;
    private String customerId;
    private String createdAt;
    private String updatedAt;
    private String closedAt;
    private ShoppingCartStatus status;
    private String totalAmount;
    private String items;
}
