package com.ataya.company.controller;

import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreDetailsResponse;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.exception.custom.ValidationException;
import com.ataya.company.model.Worker;
import com.ataya.company.service.StoreService;
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
@RequestMapping("/company/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }


    // create store
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create store",
            description = """
                    This endpoint is used to create a store. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN can create store. \s
                    """
    )
    public ResponseEntity<ApiResponse<StoreInfoResponse>> createStore(@Valid @RequestPart CreateStoreRequest createStoreRequest, @AuthenticationPrincipal Worker owner, @RequestPart(required = false) MultipartFile profilePhoto) {
        return ResponseEntity.status(201).body(storeService.createStore(createStoreRequest, owner.getCompanyId(), profilePhoto));
    }

    // get store info
    @GetMapping("/{storeId}")
    @PreAuthorize("(hasRole('ADMIN') and @storeSecurity.hasAccess(#storeId, authentication.principal.companyId)) or ((hasAnyRole('WORKER','MANAGER')) and #storeId == authentication.principal.storeId)")
    @Operation(
            summary = "Get store info",
            description = """
                    This endpoint is used to get store info. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: authenticated user can get store info. \s
                    """
    )
    public ResponseEntity<ApiResponse<StoreInfoResponse>> getStoreInfo(@PathVariable String storeId) {
        return ResponseEntity.ok(storeService.getStoreInfo(storeId));
    }

    // get stores
    @GetMapping("/Stores")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get stores",
            description = """
                    This endpoint is used to get stores. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN can get stores. \s
                    """
    )
    public ResponseEntity<ApiResponse<List<StoreInfoResponse>>> getStores(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String storeCode,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Worker owner //get company id from owner
    ) {
        if (owner.getCompanyId() == null) {
            throw new ValidationException(
                    "Company Id",
                    null,
                    "User does not belong to any company"
            );
        }
        return ResponseEntity.ok(storeService.getStores(name, storeCode, description, status, page, size, owner.getCompanyId()));
    }

    // update store
    @PutMapping("/{storeId}")
    @PreAuthorize("(hasRole('ADMIN') and @storeSecurity.hasAccess(#storeId, authentication.principal.companyId)) or ((hasRole('MANAGER')) and #storeId == authentication.principal.storeId)")
    @Operation(
            summary = "Update store",
            description = """
                    This endpoint is used to update store. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN or MANAGER can update store. \s
                    """
    )
    public ResponseEntity<ApiResponse<StoreInfoResponse>> updateStore(@PathVariable String storeId, @Valid @RequestPart UpdateStoreRequest updateStoreRequest, @RequestPart(required = false) MultipartFile profilePhoto) {
        return ResponseEntity.ok(storeService.updateStore(storeId, updateStoreRequest, profilePhoto));
    }

    // get store workers
    @GetMapping("/workers")
    @PreAuthorize("(hasRole('ADMIN') and @storeSecurity.hasAccess(#storeId, authentication.principal.companyId)) or ((hasRole('MANAGER')) and #storeId == authentication.principal.storeId)")
    @Operation(
            summary = "Get store workers",
            description = """
                    This endpoint is used to get store workers. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN or MANAGER can get store workers. \s
                    """
    )
    public ResponseEntity<ApiResponse<StoreDetailsResponse>> getStoreWorkers(
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Worker authenticatedPrincipal
            ) {
        String id = storeId == null ? authenticatedPrincipal.getStoreId() : storeId;
        return ResponseEntity.ok(storeService.getStoreWorkers(id, name, surname, username, email, phone, page, size));
    }

    // get store products
    @GetMapping("/products")
    @PreAuthorize("(hasRole('ADMIN') and @storeSecurity.hasAccess(#storeId, authentication.principal.companyId)) or ((hasRole('MANAGER')) and #storeId == authentication.principal.storeId)")
    @Operation(
            summary = "Get store products",
            description = """
                    This endpoint is used to get store products. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN or MANAGER can get store products. \s
                    """
    )
    public ResponseEntity<ApiResponse<StoreDetailsResponse>> getStoreProducts(
            @RequestParam(required = false) String storeId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) String barcode,
            @RequestParam(required = false) String upc,
            @RequestParam(required = false) String ean,
            @RequestParam(required = false) String serialNumber,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String discount,
            @RequestParam(required = false) String discountRate,
            @RequestParam(required = false) String isDiscounted,
            @RequestParam(required = false) String discountPrice,
            @RequestParam(required = false) String sz,
            @RequestParam(required = false) String weight,
            @RequestParam(required = false) String color,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Worker authenticatedPrincipal
    ) {
        String id = storeId == null ? authenticatedPrincipal.getStoreId() : storeId;
        return ResponseEntity.ok(storeService.getStoreProducts(id, name, sku, barcode, upc, ean, serialNumber, brand, category, price, discount, discountRate, isDiscounted, discountPrice, sz, weight, color, page, size));
    }

    // get store details
    @GetMapping("/details")
    @PreAuthorize("hasAnyRole('WORKER','MANAGER')")
    @Operation(
            summary = "Get store details",
            description = """
                    This endpoint is used to get store details. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role WORKER or MANAGER can get store details. \s
                    """
    )
    public ResponseEntity<ApiResponse<StoreDetailsResponse>> getStoreDetails(@AuthenticationPrincipal Worker authenticatedPrincipal, @RequestParam(required = false) String storeId) {
        String id = storeId == null ? authenticatedPrincipal.getStoreId() : storeId;
        return ResponseEntity.ok(storeService.getStoreDetails(id));
    }
}
