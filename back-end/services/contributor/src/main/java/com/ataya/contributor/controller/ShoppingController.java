package com.ataya.contributor.controller;


import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.dto.store.StoreDto;
import com.ataya.contributor.model.Contributor;
import com.ataya.contributor.service.ShoppingService;
import com.ataya.contributor.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopping")
@RequiredArgsConstructor
public class ShoppingController {

    private final ShoppingService shoppingService;


    // get products
    @GetMapping("/products")
    @Operation(summary = "Get all products")
    private ResponseEntity<ApiResponse<List<ProductItemDto>>> getProducts(
            @RequestParam(name = "strId") String storeId,
            @RequestParam(name = "nm", required = false) String name,
            @RequestParam(name = "cat", required = false) String category,
            @RequestParam(name = "min", required = false) Double minPrice,
            @RequestParam(name = "max", required = false) Double maxPrice,
            @RequestParam(required = false) String brand,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                shoppingService.getProducts(storeId, name, category, minPrice, maxPrice, brand, page, size)
        );
    }

    // put products in basket
    @PutMapping("/cart")
    @Operation(summary = "Add products to cart")
    private ResponseEntity<ApiResponse<String>> addToCart(@RequestParam String itemId, @RequestParam Double quantity, @AuthenticationPrincipal Contributor user) {
        return ResponseEntity.ok(shoppingService.addToCart(user.getId(), itemId, quantity));
    }

    // get cart items
    @GetMapping("/cart")
    @Operation(summary = "Get cart items")
    private ResponseEntity<ApiResponse<ShoppingCartDto>> getCartItems(@AuthenticationPrincipal Contributor user) {
        return ResponseEntity.ok(shoppingService.getCartItems(user.getId()));
    }

    // remove item from cart
    @DeleteMapping("/cart")
    @Operation(summary = "Remove item from cart")
    private ResponseEntity<ApiResponse<String>> removeItemFromCart(@RequestParam String itemId, @RequestParam Double quantity, @AuthenticationPrincipal Contributor user) {
        return ResponseEntity.ok(shoppingService.removeItemFromCart(user.getId(), quantity, itemId));
    }

    // get all stores
    @GetMapping("/stores")
    @Operation(summary = "Get all stores")

    private ResponseEntity<ApiResponse<List<StoreDto>>> getAllStores(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.ok(shoppingService.getAllStores(page, size));
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get store by ID")
    private ResponseEntity<ApiResponse<StoreDto>> getStoreById(@PathVariable String storeId) {
        return ResponseEntity.ok(shoppingService.getStoreById(storeId));

    }
}
