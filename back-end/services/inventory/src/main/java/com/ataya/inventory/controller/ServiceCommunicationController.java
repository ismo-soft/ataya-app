package com.ataya.inventory.controller;

import com.ataya.inventory.dto.contributor.ProductItemDto;
import com.ataya.inventory.service.ServiceCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service-communication")
@RequiredArgsConstructor
public class ServiceCommunicationController {

    private final ServiceCommunicationService serviceCommunicationService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductItemDto>> getProducts(String storeId) {
        List<ProductItemDto> products = serviceCommunicationService.getProducts(storeId);
        return ResponseEntity.ok(products);

    }
}
