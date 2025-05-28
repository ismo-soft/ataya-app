package com.ataya.inventory.service;

import com.ataya.inventory.dto.company.ProductDto;

import java.util.List;
import java.util.Set;

public interface RestService {
    List<ProductDto> getProductDtos(String ids, String companyId);
}
