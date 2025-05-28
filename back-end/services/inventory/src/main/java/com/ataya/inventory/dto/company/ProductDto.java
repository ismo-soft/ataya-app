package com.ataya.inventory.dto.company;


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
    private String category;
    private String brand;
    private String companyId;
}
