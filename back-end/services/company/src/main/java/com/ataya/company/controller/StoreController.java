package com.ataya.company.controller;


import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.exception.Custom.ValidationException;
import com.ataya.company.model.Worker;
import com.ataya.company.service.StoreService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<StoreInfoResponse>> createStore(@Valid @RequestBody CreateStoreRequest createStoreRequest, @AuthenticationPrincipal Worker owner) {
        return ResponseEntity.status(201).body(storeService.createStore(createStoreRequest, owner.getCompanyId()));
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
    public ApiResponse<StoreInfoResponse> getStoreInfo(@PathVariable String storeId) {
        return storeService.getStoreInfo(storeId);
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
    public ApiResponse<List<StoreInfoResponse>> getStores(
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
        return storeService.getStores(name, storeCode, description, status, page, size, owner.getCompanyId());
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
    public ApiResponse<StoreInfoResponse> updateStore(@PathVariable String storeId, @Valid @RequestBody UpdateStoreRequest updateStoreRequest) {
        return storeService.updateStore(storeId, updateStoreRequest);
    }

    // get store workers
    // TODO: implement get store workers

    // get store products
    // TODO: implement get store products

    // get store details
    // TODO: implement get store details
}
