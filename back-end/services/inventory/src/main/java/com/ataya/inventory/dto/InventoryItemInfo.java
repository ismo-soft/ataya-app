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
    private Double quantity;
    private String productName;
    private String productCategory;
    private String productBrand;
    private Double suspendedQuantity;
    private Double waitingForBeneficiaryQuantity;
    private Double deliveredQuantity;
    private Double reorderLevel;
    private Double lastSupplyQuantity;
    private ItemUnit unit;
    private Double price;
    private Double discount;
    private Double discountRate;
    private Double discountedPrice;
    private Boolean isDiscounted;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
