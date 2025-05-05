package com.ataya.inventory.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateInventoryRequest {
    private Double reorderLevel;
    private String unit;
    private Double price;
    private Double discount;
    private Double discountRate;
}
