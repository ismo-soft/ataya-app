package com.ataya.beneficiary.service.impl;

import com.ataya.beneficiary.dto.product.ProductItemDto;
import com.ataya.beneficiary.dto.store.StoreDto;
import com.ataya.beneficiary.dto.store.StoreDtoPage;
import com.ataya.beneficiary.exception.custom.ValidationException;
import com.ataya.beneficiary.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestServiceImpl implements RestService {

    private final RestTemplate restTemplate;

    @Value("${ataya.beneficiary.inventory-service.url}")
    private String inventoryServiceUrl;

    @Value("${ataya.beneficiary.company-service.url}")
    private String companyServiceUrl;

    @Override
    public StoreDtoPage getStores(Integer page, Integer size) {
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
    public StoreDto getStoreById(String id) {
        if (id == null || id.isEmpty()) {
            throw new ValidationException("store ID", "ID cannot be null or empty", "Invalid store ID provided");
        }
        String url = String.format("%s/store/%s", companyServiceUrl, id);
        StoreDto store = restTemplate.getForObject(url, StoreDto.class);
        if (store == null) {
            throw new ValidationException("store", id, "Store not found with the given ID.");
        }
        return store;
    }

    @Override
    public List<ProductItemDto> getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size) {
        return List.of();
    }
}
