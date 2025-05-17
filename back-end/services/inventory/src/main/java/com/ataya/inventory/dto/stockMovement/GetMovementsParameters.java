package com.ataya.inventory.dto.stockMovement;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMovementsParameters {
    private String storeId;
    private String inventoryId;
    private String type;
    private String dateRange;
    private Integer page;
    private Integer size;
}
