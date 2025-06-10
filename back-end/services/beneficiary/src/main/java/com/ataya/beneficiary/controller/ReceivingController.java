package com.ataya.beneficiary.controller;

import com.ataya.beneficiary.dto.product.ProductItemDto;
import com.ataya.beneficiary.dto.store.StoreDto;
import com.ataya.beneficiary.service.ReceivingService;
import com.ataya.beneficiary.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receiving")
public class ReceivingController {

    private final ReceivingService receivingService;

    // get all stores
    @GetMapping("/stores")
    @Operation(summary = "Get all stores")
    public ResponseEntity<ApiResponse<List<StoreDto>>> getStores(@RequestParam(required = false, defaultValue = "0") Integer page, @RequestParam(required = false, defaultValue = "10") Integer size){
        return ResponseEntity.ok(receivingService.getStores(page, size));
    }
    // get store by id
    @GetMapping("/stores/{id}")
    @Operation(summary = "Get store by id")
    public ResponseEntity<ApiResponse<StoreDto>> getStoreById(@RequestParam String id) {
        return ResponseEntity.ok(receivingService.getStoreById(id));
    }
    // get products by store id
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
                receivingService.getProducts(storeId, name, category, minPrice, maxPrice, brand, page, size)
        );
    }

}
