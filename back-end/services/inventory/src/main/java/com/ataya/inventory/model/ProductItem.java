package com.ataya.inventory.model;

import com.ataya.inventory.enums.ItemStatus;
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
public class ProductItem {
    @Id
    private String id;
    private String inventoryId;
    private String storeId;
    private String productId;
    private String sku;
    private String upc;
    private String ean;
    private ItemStatus status;
    private LocalDateTime created;
    private LocalDateTime updated;
    private String createdBy;
    private String updatedBy;

}
