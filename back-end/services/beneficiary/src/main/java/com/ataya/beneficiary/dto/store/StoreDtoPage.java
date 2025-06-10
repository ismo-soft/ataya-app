package com.ataya.beneficiary.dto.store;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDtoPage {
    private List<StoreDto> stores;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;
}
