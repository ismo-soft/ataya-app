package com.ataya.contributor.dto.shoppingCart;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInfoDto {
    private String itemId;
    private String productId;
    private String productName;
    private String productBrand;
    private String productCategory;
    private Double price;
    private Double quantity;
    private String imageUrl;

}
