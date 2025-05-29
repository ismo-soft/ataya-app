package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.exception.custom.InvalidOperationException;
import com.ataya.contributor.service.RestService;
import com.ataya.contributor.service.ShoppingService;
import com.ataya.contributor.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {

    private final RestService restService;

    @Override
    public ApiResponse<List<ProductItemDto>> getProducts(String storeId) {
        if (storeId == null || storeId.isEmpty()) {
            throw new InvalidOperationException(
                    "get products of store",
                    "Store ID cannot be null or empty"
            );
        }
        List<ProductItemDto> products = restService.getProducts(storeId);
        return ApiResponse.<List<ProductItemDto>>builder().
                data(products)
                .message("Products retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<ShoppingCartDto> addToCart(String id, List<String> items) {
        return null;
    }

    @Override
    public ApiResponse<ShoppingCartDto> getCartItems(String id) {
        return null;
    }

    @Override
    public ApiResponse<ShoppingCartDto> removeItemFromCart(String id, String itemId) {
        return null;
    }
}
