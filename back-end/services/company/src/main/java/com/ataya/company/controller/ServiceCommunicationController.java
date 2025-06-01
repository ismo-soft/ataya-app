package com.ataya.company.controller;

import com.ataya.company.dto.product.ProductDto;
import com.ataya.company.dto.store.StoreDto;
import com.ataya.company.service.ProductService;
import com.ataya.company.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/service-communication")
@RequiredArgsConstructor
public class ServiceCommunicationController {

    private final ProductService productService;
    private final StoreService storeService;


    @GetMapping("/products")
    public List<ProductDto> getProducts(
            @RequestParam String ids,
            @RequestParam String companyId
    ) {
        return productService.getProductDtos(ids, companyId);

    }

    // get all stores
    @GetMapping("/stores")
    public List<StoreDto> getAllStores() {
        return storeService.getAllStoresAsDto();
    }

    // get store by id
    @GetMapping("/store/{storeId}")
    public StoreDto getStoreById(@PathVariable String storeId) {
        return storeService.getStoreByIdAsDto(storeId);
    }
}
