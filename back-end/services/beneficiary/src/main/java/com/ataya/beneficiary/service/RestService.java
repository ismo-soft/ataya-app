package com.ataya.beneficiary.service;

import com.ataya.beneficiary.dto.product.ProductItemDtoPage;
import com.ataya.beneficiary.dto.store.StoreDto;
import com.ataya.beneficiary.dto.store.StoreDtoPage;

import java.util.List;

public interface RestService {
    StoreDtoPage getStores(Integer page, Integer size);

    StoreDto getStoreById(String id);

    ProductItemDtoPage getProducts(String storeId, String name, String category, String brand, int page, int size);
}
