package com.ataya.contributor.dto.store;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemStatistics {
    // map string to long - count of items added to cart
    private Map<String, Long> itemsAddedToCart; // map string to long - count of items added to cart

    // map string to long - count of items removed from cart
    private Map<String, Long> itemsRemovedFromCart;

    // map string to long - count of sold items
    private Map<String, Long> itemsSold;

    // map String to double - sold quantity
    private Map<String, Double> soldQuantity;

    // map String to double - price of sold items
    private Map<String, Double> soldPrice;

    // map String to double - total price of sold items
    private Map<String, Double> totalPriceOfSoldItems;
}
