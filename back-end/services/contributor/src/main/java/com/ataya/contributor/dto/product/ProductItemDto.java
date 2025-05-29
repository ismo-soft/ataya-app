package com.ataya.contributor.dto.product;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemDto {
    private String itemId;
    private String productId;
    private String productName;
    private String productBrand;
    private String productCategory;
    private double availableQuantity;
    private boolean isDiscounted;
    private double discountedPrice;
    private double price;
    private String imageUrl;

}
