package com.ataya.company.controller;


import com.ataya.company.dto.product.CreateProductRequest;
import com.ataya.company.dto.product.ProductDetailsResponse;
import com.ataya.company.dto.product.ProductInfoResponse;
import com.ataya.company.dto.product.UpdateProductRequest;
import com.ataya.company.model.Worker;
import com.ataya.company.service.ProductService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // create a new product
    @PostMapping("/create")
    @Operation(
            summary = "Create product",
            description = """
                    This endpoint is used to create a product. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN can create product. \s
                    """
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductInfoResponse>> createProduct(@Valid @RequestPart CreateProductRequest createProductRequest, @RequestPart(required = false) List<MultipartFile> images, @AuthenticationPrincipal Worker user) {
        return ResponseEntity.status(201).body(productService.createProduct(createProductRequest, images, user));
    }
    // update a product
    @PutMapping("/{id}")
    @Operation(
            summary = "Update product",
            description = """
                    This endpoint is used to update a product. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN can update product. \s
                    """
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductInfoResponse>> updateProduct(@PathVariable String id, @Valid @RequestPart UpdateProductRequest updateProductRequest, @RequestPart(required = false) List<MultipartFile> images, @AuthenticationPrincipal Worker user) {
        return ResponseEntity.ok(productService.updateProduct(id, updateProductRequest, images, user));
    }

    // get all products
    @GetMapping("/products")
    @Operation(
            summary = "Get all products",
            description = """
                    This endpoint is used to get all products. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN can get all products. \s
                    """
    )
    public ResponseEntity<ApiResponse<List<ProductInfoResponse>>> getProducts(
            @RequestParam(required = false, name = "nm") String name,
            @RequestParam(required = false, name = "bnd") String brand,
            @RequestParam(required = false, name = "cgr") String category,
            @RequestParam(required = false) String sz,
            @RequestParam(required = false, name = "wgt") String weight,
            @RequestParam(required = false, name = "clr") String color,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Worker user
    ) {
        return ResponseEntity.ok(productService.getProducts(name, brand, category, sz, weight, color, page, size,user.getCompanyId(), user.getStoreId()));
    }


    // get a product by id
    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by id",
            description = """
                    This endpoint is used to get a product by id. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN can get a product by id. \s
                    """
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductInfoResponse>> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/details/{id}")
    @Operation(
            summary = "Get product details by id",
            description = """
                    This endpoint is used to get product details by id. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN can get product details by id. \s
                    """
    )
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<ProductDetailsResponse>> getProductDetailsById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductDetailsById(id));
    }


}
