package com.ataya.inventory.dto.stockMovement;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovementInfo {
    private String id;
    private String storeId;
    private String inventoryId;
    private String type;
    private double quantity;
    private String note;
    private String reason;
    private String referenceId;
    private String happenedAt;
    private String by;
}
