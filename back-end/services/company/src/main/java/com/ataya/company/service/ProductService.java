package com.ataya.company.service;

import com.ataya.company.dto.product.CreateProductRequest;
import com.ataya.company.dto.product.ProductDetailsResponse;
import com.ataya.company.dto.product.ProductInfoResponse;
import com.ataya.company.dto.product.UpdateProductRequest;
import com.ataya.company.model.Worker;
import com.ataya.company.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ApiResponse<ProductInfoResponse> createProduct(@Valid CreateProductRequest createProductRequest, List<MultipartFile> images, Worker user);

    ApiResponse<ProductInfoResponse> updateProduct(String id, @Valid UpdateProductRequest request, List<MultipartFile> images, Worker worker);

    ApiResponse<List<ProductInfoResponse>> getProducts(String name, String sku, String barcode, String upc, String ean, String serialNumber, String brand, String category, String price, String discount, String discountRate, String isDiscounted, String discountPrice, String sz, String weight, String color, int page, int size,String companyId, String storeId);

    ApiResponse<ProductInfoResponse> getProductById(String id);

    ApiResponse<ProductDetailsResponse> getProductDetailsById(String id);
}
