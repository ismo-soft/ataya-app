package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.product.ProductItemDto;
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

    @Override
    public List<ProductItemDto> getProducts(String storeId) {
        String url = String.format("%s/products?storeId=%s", inventoryServiceUrl, storeId);
        ProductItemDto[] products = restTemplate.getForObject(url, ProductItemDto[].class);
        return List.of(products != null ? products : new ProductItemDto[0]);

    }
}
