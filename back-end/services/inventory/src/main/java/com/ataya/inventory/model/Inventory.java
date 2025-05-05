package com.ataya.inventory.model;


import com.ataya.inventory.enums.ItemUnit;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Inventory {
    @Id
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
    private Double discountedPrice;
    private Boolean isDiscounted;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
