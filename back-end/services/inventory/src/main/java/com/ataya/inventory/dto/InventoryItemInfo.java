package com.ataya.inventory.dto;

import com.ataya.inventory.enums.ItemUnit;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryItemInfo {
    private String id;
    private String storeId;
    private String productId;
    private String companyId;
    private Double quantity;
    private Double reorderLevel;
    private Double lastSupplyQuantity;
    private ItemUnit unit;
    private Double price;
    private Double discount;
    private Double discountRate;
    private Boolean isDiscounted;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
