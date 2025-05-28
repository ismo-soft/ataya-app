package com.ataya.company.controller;

import com.ataya.company.dto.product.ProductDto;
import com.ataya.company.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/service-communication")
@RequiredArgsConstructor
public class ServiceCommunicationController {

    private final ProductService productService;


    @GetMapping("/products")
    public List<ProductDto> getProducts(
            @RequestParam String ids,
            @RequestParam String companyId
    ) {
        return productService.getProductDtos(ids, companyId);

    }
}
