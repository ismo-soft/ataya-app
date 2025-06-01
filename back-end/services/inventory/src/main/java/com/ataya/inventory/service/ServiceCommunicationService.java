package com.ataya.inventory.service;

import com.ataya.inventory.dto.contributor.ItemInfoDto;
import com.ataya.inventory.dto.contributor.ProductItemDto;

import java.util.List;

public interface ServiceCommunicationService {
    List<ProductItemDto> getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size);

    Double getProductPrice(String itemId);

    List<ItemInfoDto> getProducts(String items);
}
