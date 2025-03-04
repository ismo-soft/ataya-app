package com.ataya.company.controller;

import com.ataya.company.service.ProductService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequestMapping("/company/product")
public class ProductController {
//    /*
//    *
//    * 1. create product
//    * 2. update product
//    * 3. delete product
//    * 4. get product by id
//    * 5. get all store products
//    * 6. get all store products by category
//    * 7. update product price
//    * 8. update products prices
//    * 9. set discount to product (rate or amount)
//    * 10. set discount to products (rate or amount)
//    * 11. get product price (real price or discounted price)
//    * 12. get products prices (real price or discounted price)
//    **/
//
//    @Autowired
//    private ProductService productService;
//
//    @PostMapping("/create")
//    @Operation(
//            summary = "Create product",
//            description = "Create product"
//    )
//    public ResponseEntity<ApiResponse> createProduct(@RequestBody CreateProductRequest request, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.createProduct(request, user));
//    }
//
//    @PutMapping("/update")
//    @Operation(
//            summary = "Update product",
//            description = "Update product"
//    )
//    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.updateProduct(request, user));
//    }
//
//    @DeleteMapping("/delete/{productId}")
//    @Operation(
//            summary = "Delete product",
//            description = "Delete product"
//    )
//    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable String productId, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.deleteProduct(productId, user));
//    }
//
//    @GetMapping("/get/{productId}")
//    @Operation(
//            summary = "Get product by id",
//            description = "Get product by id"
//    )
//    public ResponseEntity<ApiResponse> getProductById(@PathVariable String productId, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.getProductById(productId, user));
//    }
//
//    @GetMapping("/get/all/{storeId}")
//    @Operation(
//            summary = "Get all store products",
//            description = "Get all store products"
//    )
//    public ResponseEntity<ApiResponse> getAllStoreProducts(@PathVariable String storeId, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.getAllStoreProducts(storeId, user));
//    }
//
//    @GetMapping("/get/all/{storeId}/{category}")
//    @Operation(
//            summary = "Get all store products by category",
//            description = "Get all store products by category"
//    )
//    public ResponseEntity<ApiResponse> getAllStoreProductsByCategory(@PathVariable String storeId, @PathVariable String category, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.getAllStoreProductsByCategory(storeId, category, user));
//    }
//
//    @PutMapping("/update/price/{productId}")
//    @Operation(
//            summary = "Update product price",
//            description = "Update product price"
//    )
//    public ResponseEntity<ApiResponse> updateProductPrice(@PathVariable String productId, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.updateProductPrice(productId, user));
//    }
//
//    @PutMapping("/update/prices")
//    @Operation(
//            summary = "Update products prices",
//            description = "Update products prices"
//    )
//    public ResponseEntity<ApiResponse> updateProductsPrices(@RequestBody ProductList request, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.updateProductsPrices(request, user));
//    }
//
//    @PutMapping("/set/discount")
//    @Operation(
//            summary = "Set discount to product (rate or amount)",
//            description = "Set discount to product (rate or amount)"
//    )
//    public ResponseEntity<ApiResponse> setDiscountToProduct(@RequestParam double amount, @RequestParam double rate, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.setDiscountToProduct(amount, rate, user));
//    }
//
//    @PutMapping("/set/discount/products")
//    @Operation(
//            summary = "Set discount to products (rate or amount)",
//            description = "Set discount to products (rate or amount)"
//    )
//    public ResponseEntity<ApiResponse> setDiscountToProducts(@RequestBody ProductList request, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.setDiscountToProducts(request, user));
//    }
//
//    @GetMapping("/get/price/{productId}")
//    @Operation(
//            summary = "Get product price (real price or discounted price)",
//            description = "Get product price (real price or discounted price)"
//    )
//    public ResponseEntity<ApiResponse> getProductPrice(@PathVariable String productId, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.getProductPrice(productId, user));
//    }
//
//    @GetMapping("/get/prices")
//    @Operation(
//            summary = "Get products prices (real price or discounted price)",
//            description = "Get products prices (real price or discounted price)"
//    )
//    public ResponseEntity<ApiResponse> getProductsPrices(@RequestBody ProductList request, @AuthenticationPrincipal UserPrincipal user) {
//        return ResponseEntity.ok(productService.getProductsPrices(request, user));
//    }
//
//
}
