package com.ataya.contributor.controller;


import com.ataya.contributor.dto.product.ProductItemDto;
import com.ataya.contributor.dto.shoppingCart.ShoppingCartDto;
import com.ataya.contributor.model.Contributor;
import com.ataya.contributor.service.ShoppingService;
import com.ataya.contributor.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping")
@RequiredArgsConstructor
public class ShoppingController {

    private final ShoppingService shoppingService;


    // get products
    @GetMapping("/products")
    @Operation(summary = "Get all products")
    private ResponseEntity<ApiResponse<List<ProductItemDto>>> getProducts(@RequestParam(name = "strId") String storeId) {
        return ResponseEntity.ok( shoppingService.getProducts(storeId));
    }

    // put products in basket
    @PutMapping("/cart")
    @Operation(summary = "Add products to cart")
    private ResponseEntity<ApiResponse<ShoppingCartDto>> addToCart(@RequestBody List<String> items, @AuthenticationPrincipal String id) {
        return ResponseEntity.ok(shoppingService.addToCart(id, items));
    }

    // get cart items
    @GetMapping("/cart")
    @Operation(summary = "Get cart items")
    private ResponseEntity<ApiResponse<ShoppingCartDto>> getCartItems(@AuthenticationPrincipal Contributor user) {
        return ResponseEntity.ok(shoppingService.getCartItems(user.getId()));
    }

    // remove item from cart
    @DeleteMapping("/cart/{itemId}")
    @Operation(summary = "Remove item from cart")
    private ResponseEntity<ApiResponse<ShoppingCartDto>> removeItemFromCart(@PathVariable String itemId, @AuthenticationPrincipal Contributor user) {
        return ResponseEntity.ok(shoppingService.removeItemFromCart(user.getId(), itemId));
    }

}
