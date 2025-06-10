package com.ataya.beneficiary.service.impl;

import com.ataya.beneficiary.dto.product.ProductItemDto;
import com.ataya.beneficiary.dto.store.StoreDto;
import com.ataya.beneficiary.dto.store.StoreDtoPage;
import com.ataya.beneficiary.exception.custom.ResourceNotFoundException;
import com.ataya.beneficiary.service.ReceivingService;
import com.ataya.beneficiary.service.RestService;
import com.ataya.beneficiary.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReceivingServiceImpl implements ReceivingService {

    private final RestService restService;

    @Override
    public ApiResponse<List<StoreDto>> getStores(Integer page, Integer size) {
        StoreDtoPage dtoPage = restService.getStores(page, size);
        if (dtoPage == null || dtoPage.getStores() == null || dtoPage.getStores().isEmpty()) {
            throw new ResourceNotFoundException(
                    "stores",
                    dtoPage == null ? "null" : "empty",
                    "No stores found or the store list is empty."
            );
        }
        return ApiResponse.<List<StoreDto>>builder()
                .data(dtoPage.getStores())
                .message("Stores retrieved successfully.")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .page(dtoPage.getPageNumber())
                .size(dtoPage.getPageSize())
                .total(dtoPage.getTotalElements())
                .totalPages(dtoPage.getTotalPages())
                .build();
    }

    @Override
    public ApiResponse<StoreDto> getStoreById(String id) {
        StoreDto store = restService.getStoreById(id);
        if (store == null) {
            throw new ResourceNotFoundException("store", id, "Store not found with the given ID.");
        }
        return ApiResponse.<StoreDto>builder()
                .data(store)
                .message("Store retrieved successfully.")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<List<ProductItemDto>> getProducts(String storeId, String name, String category, Double minPrice, Double maxPrice, String brand, int page, int size) {
        List<ProductItemDto> products = restService.getProducts(storeId, name, category, minPrice, maxPrice, brand, page, size);
        if (products == null || products.isEmpty()) {
            throw new ResourceNotFoundException(
                    "products",
                    "storeId: " + storeId + ", name: " + name + ", category: " + category,
                    "No products found for the given criteria."
            );
        }
        return ApiResponse.<List<ProductItemDto>>builder()
                .data(products)
                .message("Products retrieved successfully.")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
