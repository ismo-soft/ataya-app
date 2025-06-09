package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.store.StoreDto;
import com.ataya.contributor.dto.store.StoreDtoPage;
import com.ataya.contributor.exception.custom.ValidationException;
import com.ataya.contributor.model.CartItem;
import com.ataya.contributor.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

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
    public StoreDtoPage getAllStores(Integer page, Integer size) {
        if (page == null || size == null) {
            throw new ValidationException("pagination parameters", "Page and size cannot be null", "Invalid pagination parameters provided");
        }
        String url = String.format("%s/stores?page=%d&size=%d", companyServiceUrl, page, size);
        StoreDtoPage storeDtoPage = restTemplate.getForObject(url, StoreDtoPage.class);
        if (storeDtoPage == null || storeDtoPage.getStores() == null) {
            throw new ValidationException("stores", "No stores found", "No stores available in the system");
        }
        return storeDtoPage;
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

    @Override
    public Map<String, Boolean> areItemsAvailableToBuy(Map<String, Double> itemsToQuantity) {
        if (itemsToQuantity == null || itemsToQuantity.isEmpty()) {
            throw new ValidationException("items to quantity", "Items to quantity map cannot be null or empty", "Invalid items to quantity provided");
        }
        StringBuilder urlBuilder = new StringBuilder(inventoryServiceUrl + "/product/availability?");

        itemsToQuantity.forEach((itemId, quantity) -> {
            if (itemId != null && !itemId.isEmpty() && quantity != null && quantity > 0) {
                // items=itemId,quantity&items=itemId2,quantity2&...
                urlBuilder.append("items=").append(itemId).append(",").append(quantity).append(";");
            }
        });

        String url = urlBuilder.toString();
        System.out.println("Checking item availability with URL: " + url);

        Map<String, Boolean> availabilityMap = restTemplate.getForObject(url, Map.class);
        if (availabilityMap == null || availabilityMap.isEmpty()) {
            throw new ValidationException("item availability", "No availability data found", "No item availability data returned from the service");
        }
        return availabilityMap;
    }


    private String addIfNotNull(String url, String paramName, Object paramValue) {
        if (paramValue != null) {
            return url + (url.contains("?") ? "&" : "?") + paramName + "=" + paramValue;
        }
        return url;
    }


}
