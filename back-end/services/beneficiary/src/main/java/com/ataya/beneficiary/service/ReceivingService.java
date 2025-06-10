package com.ataya.beneficiary.service;

import com.ataya.beneficiary.dto.product.ProductItemDto;
import com.ataya.beneficiary.dto.store.StoreDto;
import com.ataya.beneficiary.util.ApiResponse;

import java.util.List;

public interface ReceivingService {
    ApiResponse<List<StoreDto>> getStores(Integer page, Integer size);

    ApiResponse<StoreDto> getStoreById(String id);

    ApiResponse<List<ProductItemDto>> getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size);
}
