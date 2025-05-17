package com.ataya.inventory.dto.company;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDto {
    private String id;
    private String name;
    private String productCategory;
    private String brand;
    private String companyId;
    private String correlationId;
    private String username;
}
