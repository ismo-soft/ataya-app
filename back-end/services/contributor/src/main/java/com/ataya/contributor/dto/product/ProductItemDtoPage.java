package com.ataya.contributor.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemDtoPage {
    private List<ProductItemDto> products;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;
}
