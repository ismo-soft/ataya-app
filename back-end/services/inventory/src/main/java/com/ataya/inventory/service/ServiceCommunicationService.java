package com.ataya.inventory.service;

import com.ataya.inventory.dto.contributor.ProductItemDto;

import java.util.List;

public interface ServiceCommunicationService {
    List<ProductItemDto> getProducts(String storeId);
}
