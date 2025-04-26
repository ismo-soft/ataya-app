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
    private ProductCategory category;
    private String brand;
    private List<String> images;
    private double size;
    private double weight;
    private String color;
    private String companyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}