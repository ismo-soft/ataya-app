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
    private String companyId;
    private List<String> storeIds;
}
