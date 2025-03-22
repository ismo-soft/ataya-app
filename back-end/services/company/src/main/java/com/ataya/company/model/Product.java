package com.ataya.company.model;


import com.ataya.company.enums.ProductCategory;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "product")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String sku;
    private String barcode;
    private String upc;
    private String ean;
    private String brand;
    private ProductCategory category;
    private double price;
    private double discount;
    private double discountRate;
    private boolean isDiscounted;
    private double discountPrice;
    private double size;
    private double weight;
    private String color;
    private List<String> images;
    private String companyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
