package com.ataya.inventory.dto.stockMovement;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditQuantityRequest {
    private String storeId;
    private String inventoryId;
    private Double newQuantity;
    private String note;
    private String reason;
}
