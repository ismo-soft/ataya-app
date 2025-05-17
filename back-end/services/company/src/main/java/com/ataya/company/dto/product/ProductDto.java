package com.ataya.company.dto.product;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String id;
    private String name;
    private String productCategory;
    private String brand;
    private String companyId;
    private String correlationId;
    private String username;
}
