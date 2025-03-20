package com.ataya.company.dto.product;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    private String name;
    private String description;
    private String sku;
    private String upc;
    private String ean;
    private String serialNumber;
    private String barcode;
    private String brand;
    private String category;
    @NotNull(message = "Price is required")
    private double price;
    private double discount;
    private double discountRate;
    private double discountPrice;
    private double size;
    private double weight;
    private String color;
}
