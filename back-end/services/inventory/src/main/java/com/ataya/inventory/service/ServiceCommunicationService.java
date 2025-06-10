package com.ataya.inventory.service;

import com.ataya.inventory.dto.contributor.ItemInfoDto;
import com.ataya.inventory.dto.contributor.ProductItemDto;
import com.ataya.inventory.dto.contributor.ProductItemDtoPage;

import java.util.List;
import java.util.Map;

public interface ServiceCommunicationService {
    ProductItemDtoPage getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size);

    Double getProductPrice(String itemId);

    List<ItemInfoDto> getProducts(String items);

    Map<String, Boolean> areItemsAvailableToBuy(List<String> items);

    ProductItemDtoPage getProductsToDeliver(String storeId, String name, String category, String brand, int page, int size);
}
