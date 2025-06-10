package com.ataya.inventory.controller;

import com.ataya.inventory.dto.contributor.ItemInfoDto;
import com.ataya.inventory.dto.contributor.ProductItemDto;
import com.ataya.inventory.dto.contributor.ProductItemDtoPage;
import com.ataya.inventory.service.ServiceCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/service-communication")
@RequiredArgsConstructor
public class ServiceCommunicationController {

    private final ServiceCommunicationService serviceCommunicationService;

    @GetMapping("/products")
    public ResponseEntity<ProductItemDtoPage> getProducts(
            @RequestParam(name = "strId") String storeId,
            @RequestParam(name = "nm", required = false) String name,
            @RequestParam(name = "cat", required = false) String category,
            @RequestParam(name = "nPrc", required = false) Double minPrice,
            @RequestParam(name = "xPrc", required = false) Double maxPrice,
            @RequestParam(required = false) String brand,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        ProductItemDtoPage products = serviceCommunicationService.getProducts(storeId, name, category, minPrice, maxPrice, brand, page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/product/items")
    public ResponseEntity<List<ItemInfoDto>> getProductsByItems(@RequestParam String items) {
        List<ItemInfoDto> productItems = serviceCommunicationService.getProducts(items);
        return ResponseEntity.ok(productItems);
    }

    @GetMapping("/product/availability")
    public ResponseEntity<Map<String, Boolean>> areItemsAvailableToBuy(@RequestParam("items") String itemsRaw) {
        List<String> items = Arrays.asList(itemsRaw.split(";")); // Use semicolon or another separator
        return ResponseEntity.ok(serviceCommunicationService.areItemsAvailableToBuy(items));
    }

    @GetMapping("/products/to-deliver")
    public ResponseEntity<ProductItemDtoPage> getProductsToDeliver(
            @RequestParam(name = "strId") String storeId,
            @RequestParam(name = "nm", required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(name = "cat", required = false) String category,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        ProductItemDtoPage products = serviceCommunicationService.getProductsToDeliver(storeId, name, category,brand, page, size);
        return ResponseEntity.ok(products);
    }

}
