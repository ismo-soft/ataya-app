package com.ataya.company.service.impl;

import com.ataya.company.dto.product.*;
import com.ataya.company.enums.ProductCategory;
import com.ataya.company.exception.custom.InvalidOperationException;
import com.ataya.company.exception.custom.ResourceNotFoundException;
import com.ataya.company.exception.custom.ValidationException;
import com.ataya.company.mapper.ProductMapper;
import com.ataya.company.model.Product;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.ProductRepository;
import com.ataya.company.service.ProductService;
import com.ataya.company.util.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.ataya.company.service.impl.CommonService.addCriteria;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CommonService commonService;

    private final FileServiceImpl fileService;

    private final ProductMapper productMapper;


    public ProductServiceImpl(ProductRepository productRepository, CommonService commonService, FileServiceImpl fileService, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.commonService = commonService;
        this.fileService = fileService;
        this.productMapper = productMapper;
    }


    @Override
    public ApiResponse<ProductInfoResponse> createProduct(CreateProductRequest createProductRequest, List<MultipartFile> images, Worker user) {
        ProductCategory category = null;
        if(createProductRequest.getCategory() != null) {
            category = ProductCategory.isCategoryExist(createProductRequest.getCategory()) ? ProductCategory.valueOf(createProductRequest.getCategory()) : ProductCategory.OTHERS;
        }

        Product product = Product.builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .brand(createProductRequest.getBrand())
                .category(category)
                .size(createProductRequest.getSize())
                .weight(createProductRequest.getWeight())
                .color(createProductRequest.getColor())
                .companyId(user.getCompanyId())
                .createdBy(user.getUsername())
                .createdAt(LocalDateTime.now())
                .build();


        product = productRepository.save(product);
        if(images != null && !images.isEmpty()) {
            List<String> imageUrls = fileService.saveImageFiles(images, "product", user.getCompanyId(), product.getId());
            product.setImages(imageUrls);
        }
        productRepository.save(product);
        return ApiResponse.<ProductInfoResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .message("Product created successfully")
                .timestamp(LocalDateTime.now())
                .data(productMapper.toProductInfoResponse(product))
                .build();

    }

    @Override
    public ApiResponse<ProductInfoResponse> updateProduct(String id, UpdateProductRequest request, List<MultipartFile> images, Worker worker) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Product",
                        "id",
                        id,
                        "Product not found"
                )
        );
        if(!product.getCompanyId().equals(worker.getCompanyId())) {
            throw new InvalidOperationException("Product", "You are not allowed to update this product");
        }
        ProductCategory category = null;
        if(request.getCategory() != null) {
            category = ProductCategory.isCategoryExist(request.getCategory()) ? ProductCategory.valueOf(request.getCategory()) : ProductCategory.OTHERS;
        }
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setCategory(category);
        product.setSize(request.getSize());
        product.setWeight(request.getWeight());
        product.setColor(request.getColor());
        product.setUpdatedBy(worker.getUsername());
        product.setUpdatedAt(LocalDateTime.now());

        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = fileService.saveImageFiles(images, "product", worker.getCompanyId(), product.getId());
            product.setImages(imageUrls);
        }
        productRepository.save(product);
        return ApiResponse.<ProductInfoResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Product updated successfully")
                .timestamp(LocalDateTime.now())
                .data(productMapper.toProductInfoResponse(product))
                .build();
    }


    @Override
    public ApiResponse<List<ProductInfoResponse>> getProducts(String name, String brand, String category, String sz, String weight, String color, int page, int size, String companyId, String storeId) {
        List<Criteria> criteriaList = new ArrayList<>();
        addCriteria(criteriaList, "name", name);
        addCriteria(criteriaList, "brand", brand);
        addStatusCriteria(criteriaList, category);
        addCriteria(criteriaList, "size", sz);
        addCriteria(criteriaList, "weight", weight);
        addCriteria(criteriaList, "color", color);
        criteriaList.add(Criteria.where("companyId").is(companyId));
        criteriaList.add(Criteria.where("storeId").is(storeId));

        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));

        long total = productRepository.countProductByQuery(query);
        if (size <= 0) {
            size = total == 0 ? 1 : (int) total;
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        query.with(pageRequest);
        List<Product> products = productRepository.findProductsByQuery(query);

        return ApiResponse.<List<ProductInfoResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .total(total)
                .page(page)
                .size(size)
                .statusCode(HttpStatus.OK.value())
                .message("Products retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(products.stream().map(productMapper::toProductInfoResponse).toList())
                .build();

    }

    private void addStatusCriteria(List<Criteria> criteriaList, String category) {
        if (category != null && !category.isEmpty()) {
            List<ProductCategory> categories = new ArrayList<>();
            for (String cat : category.split(",")) {
                if (ProductCategory.isCategoryExist(cat)) {
                    categories.add(ProductCategory.valueOf(cat));
                } else {
                    throw new ValidationException(
                            "Product category",
                            cat,
                            "Invalid product category"
                    );
                }
            }
            List<Criteria> categoryCriteria = categories.stream()
                    .map(cat -> Criteria.where("category").is(cat))
                    .toList();
            criteriaList.add(new Criteria().orOperator(categoryCriteria.toArray(new Criteria[0])));
        }
    }

    @Override
    public ApiResponse<ProductInfoResponse> getProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Product",
                        "id",
                        id,
                        "Product not found"
                )
        );
        return ApiResponse.<ProductInfoResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Product retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(productMapper.toProductInfoResponse(product))
                .build();
    }

    @Override
    public ApiResponse<ProductDetailsResponse> getProductDetailsById(String id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Product",
                        "id",
                        id,
                        "Product not found"
                )
        );
        return ApiResponse.<ProductDetailsResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Product details retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(
                        ProductDetailsResponse.builder()
                                .info(productMapper.toProductInfoResponse(product))
                                .company(commonService.getCompanyById(product.getCompanyId()))
                                .createdBy(commonService.getWorkerById(product.getCreatedBy()))
                                .updatedBy(commonService.getWorkerById(product.getUpdatedBy()))
                                .build()
                )
                .build();
    }

    @Override
    public Product getProductEntityById(String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @Override
    public List<ProductDto> getProductDtos(String ids, String companyId) {
        if(ids == null || ids.isEmpty()) {
            return List.of();
        }
        Set<String> idSet = Set.of(ids.split(","));
        List<Product> products = productRepository.findAllByIdInAndCompanyId(idSet, companyId);
        if(products.isEmpty()) {
            return List.of();
        }
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            if(product.getCompanyId().equals(companyId)) {
                productDtos.add(productMapper.toProductDto(product));
            }
        }
        return productDtos;
    }


}
