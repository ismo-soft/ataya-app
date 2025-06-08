package com.ataya.contributor.service;

import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.store.StoreDto;
import com.ataya.contributor.dto.store.StoreDtoPage;
import com.ataya.contributor.model.CartItem;

import java.util.List;

public interface RestService {

    List<ProductItemDto> getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size);

    List<CartItem> getProducts(String items);

    StoreDtoPage getAllStores(Integer page, Integer size);

    StoreDto getStoreById(String storeId);
}
