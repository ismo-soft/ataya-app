package com.ataya.company.dto.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductInfoRequestDto {
    private String productId;
    private String storeId;
    private double quantity;
    private String correlationId;
    private String username;
}
