package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.shoppingCart.ItemInfoDto;
import com.ataya.contributor.dto.store.StoreDto;
import com.ataya.contributor.exception.custom.ValidationException;
import com.ataya.contributor.model.CartItem;
import com.ataya.contributor.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestServiceImpl implements RestService {

    private final RestTemplate restTemplate;

    @Value("${ataya.contributor.inventory-service.url}")
    private String inventoryServiceUrl;

    @Value("${ataya.contributor.company-service.url}")
    private String companyServiceUrl;

    @Override
    public List<ProductItemDto> getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size) {
        String url = String.format("%s/products?strId=%s", inventoryServiceUrl, storeId);
        url = addIfNotNull(url, "nm", name);
        url = addIfNotNull(url, "cat", category);
        url = addIfNotNull(url, "nPrc", minPrice);
        url = addIfNotNull(url, "xPrice", maxPrice);
        url = addIfNotNull(url, "brand", brand);
        url = addIfNotNull(url, "page", page);
        url = addIfNotNull(url, "size", size);

        ProductItemDto[] products = restTemplate.getForObject(url, ProductItemDto[].class);
        return List.of(products != null ? products : new ProductItemDto[0]);

    }

    @Override
    public List<CartItem> getProducts(String items) {
        String url = String.format("%s/product/items?items=%s", inventoryServiceUrl, items);
        System.out.println("Fetching product items from URL: " + url);
        CartItem[] itemInfos = restTemplate.getForObject(url, CartItem[].class);
        if (itemInfos == null || itemInfos.length == 0) {
            throw new ValidationException("product items", "items: " + items, "No items found for the provided IDs");
        }
        return List.of(itemInfos);
    }

    @Override
    public List<StoreDto> getAllStores() {
        String url = String.format("%s/stores", companyServiceUrl);
        StoreDto[] stores = restTemplate.getForObject(url, StoreDto[].class);
        if (stores == null || stores.length == 0) {
            throw new ValidationException("stores", "No stores found", "No stores available in the system");
        }
        return List.of(stores);
    }

    @Override
    public StoreDto getStoreById(String storeId) {
        if (storeId == null || storeId.isEmpty()) {
            throw new ValidationException("store ID", "Store ID cannot be null or empty", "Invalid store ID provided");
        }
        String url = String.format("%s/store/%s", companyServiceUrl, storeId);
        StoreDto store = restTemplate.getForObject(url, StoreDto.class);
        if (store == null) {
            throw new ValidationException("store ID", "Store not found for ID: " + storeId, "No store found with the provided ID");
        }
        return store;
    }


    private String addIfNotNull(String url, String paramName, Object paramValue) {
        if (paramValue != null) {
            return url + (url.contains("?") ? "&" : "?") + paramName + "=" + paramValue;
        }
        return url;
    }


}
