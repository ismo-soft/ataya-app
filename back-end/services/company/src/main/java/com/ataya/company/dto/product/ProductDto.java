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
    private String companyId;
    private List<String> storeIds;
}
