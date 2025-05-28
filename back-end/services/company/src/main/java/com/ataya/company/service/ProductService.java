package com.ataya.company.service;

import com.ataya.company.dto.product.*;
import com.ataya.company.model.Product;
import com.ataya.company.model.Worker;
import com.ataya.company.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ProductService {
    ApiResponse<ProductInfoResponse> createProduct(@Valid CreateProductRequest createProductRequest, List<MultipartFile> images, Worker user);

    ApiResponse<ProductInfoResponse> updateProduct(String id, @Valid UpdateProductRequest request, List<MultipartFile> images, Worker worker);

    ApiResponse<List<ProductInfoResponse>> getProducts(String name, String brand, String category, String sz, String weight, String color, int page, int size,String companyId, String storeId);

    ApiResponse<ProductInfoResponse> getProductById(String id);

    ApiResponse<ProductDetailsResponse> getProductDetailsById(String id);

    Product getProductEntityById(String productId);

    List<ProductDto> getProductDtos(String ids, String companyId);
}
