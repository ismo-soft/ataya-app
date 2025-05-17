package com.ataya.inventory.dto.company;

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
