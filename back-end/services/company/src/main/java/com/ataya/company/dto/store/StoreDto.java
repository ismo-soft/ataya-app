package com.ataya.company.dto.store;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {
    private String id;
    private String name;
    private String companyId;
    private List<String> productIds;

}
