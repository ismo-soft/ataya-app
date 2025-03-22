package com.ataya.company.dto.product;


import com.ataya.company.enums.ProductCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInfoResponse {
    private String id;
    private String name;
    private String description;
    private String sku;
    private String barcode;
    private String upc;
    private String ean;
    private ProductCategory category;
    private String brand;
    private double price;
    private double discount;
    private double discountRate;
    private boolean isDiscounted;
    private double discountPrice;
    private double size;
    private double weight;
    private String color;
    private List<String> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
