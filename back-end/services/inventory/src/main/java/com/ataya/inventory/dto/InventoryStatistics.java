package com.ataya.inventory.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryStatistics {
    private Integer allProducts;
    private Integer inReorderLevel;
    private Integer inStock;
    private Integer outOfStock;
    private Integer inDiscount;
}
