package com.ataya.company.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Inventory {

    @Id
    private String id;
    private Double quantity;
    private Double reorderLevel;
    private String description;
    private Double receivedQuantity;
    private String barcode;
    private String sku;
    private String upc;
    private String ean;
    private String serialNumber;
    private String productId;
    private String storeId;

}
