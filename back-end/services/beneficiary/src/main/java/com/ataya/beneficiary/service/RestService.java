package com.ataya.beneficiary.service;


import com.ataya.beneficiary.dto.product.ProductItemDto;
import com.ataya.beneficiary.dto.store.StoreDto;
import com.ataya.beneficiary.dto.store.StoreDtoPage;

import java.util.List;

public interface RestService {
    StoreDtoPage getStores(Integer page, Integer size);

    StoreDto getStoreById(String id);

    List<ProductItemDto> getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size);
}
