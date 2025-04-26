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
    private ProductCategory category;
    private String brand;
    private double size;
    private double weight;
    private String color;
    private List<String> images;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
