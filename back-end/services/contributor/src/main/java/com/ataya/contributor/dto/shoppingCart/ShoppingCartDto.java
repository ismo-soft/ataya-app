package com.ataya.contributor.dto.shoppingCart;

import com.ataya.contributor.model.CartItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShoppingCartDto {
    private String id;
    private String customerId;
    private Double totalAmount;
    private List<CartItem> items;
}
