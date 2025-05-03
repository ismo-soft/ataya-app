package com.ataya.inventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInventoryRequest {
    private Double quantity;
    private Double reorderLevel;
    private Double lastSupplyQuantity;
    private String unit;
    private Double price;
    private Double discount;
    private Double discountRate;
    private Boolean isDiscounted;
}
