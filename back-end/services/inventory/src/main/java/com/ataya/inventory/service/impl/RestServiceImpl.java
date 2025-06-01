package com.ataya.inventory.service.impl;

import com.ataya.inventory.dto.company.ProductDto;
import com.ataya.inventory.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestServiceImpl implements RestService {

    private final RestTemplate restTemplate;

    @Value(
            "${ataya.inventory.product-service.url}"
    )
    private String productServiceUrl;

    @Override
    public List<ProductDto> getProductDtos(String ids, String companyId) {
        String url = productServiceUrl + "/products?ids=" + String.join(",", ids) + "&companyId=" + companyId;
        url = url.replace(" ", "");
        System.out.println("Fetching product details from URL: " + url);
        ProductDto[] productDtos = restTemplate.getForObject(url, ProductDto[].class);
        if(productDtos == null) {
            return List.of();
        }
        return List.of(productDtos);

    }
}
