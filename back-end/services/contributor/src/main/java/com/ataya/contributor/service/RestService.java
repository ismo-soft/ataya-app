package com.ataya.contributor.service;

import com.ataya.contributor.dto.product.ProductItemDto;

import java.util.List;

public interface RestService {

    List<ProductItemDto> getProducts(String storeId);
}
