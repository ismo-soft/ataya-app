package com.ataya.company.service.kafka.consumer;


import com.ataya.company.dto.product.ProductDto;
import com.ataya.company.dto.product.ProductInfoRequestDto;
import com.ataya.company.model.Product;
import com.ataya.company.repo.ProductRepository;
import com.ataya.company.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductRequestConsumer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProductService productService;

    @KafkaListener(topics = "request-product-details", groupId = "company-group")
    public void consume(String jsonMessage) throws JsonProcessingException {
        ProductInfoRequestDto request = objectMapper.readValue(jsonMessage, ProductInfoRequestDto.class);

        Product product = productService.getProductEntityById(request.getProductId());

        if (product != null) {
            ProductDto productDto = new ProductDto();
            productDto.setUsername(request.getUsername());
            productDto.setCompanyId(product.getCompanyId());
            productDto.setCorrelationId(request.getCorrelationId());
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setProductCategory(product.getCategory().name());
            productDto.setBrand(product.getBrand());

            String responseJson = objectMapper.writeValueAsString(productDto);
            kafkaTemplate.send("product-details-response", request.getCorrelationId(), responseJson);
        } else {
            kafkaTemplate.send("product-details-response", request.getCorrelationId(), null);
        }
    }

}
