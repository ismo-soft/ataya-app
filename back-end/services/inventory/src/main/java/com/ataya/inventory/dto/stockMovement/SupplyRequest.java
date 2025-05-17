package com.ataya.inventory.dto.stockMovement;


import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplyRequest {
    private String storeId;
    private Map<String, Double> product_quantity;
    private String note; 
    private String reason;
}
