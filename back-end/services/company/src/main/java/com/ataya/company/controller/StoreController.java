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
@RequestMapping("/store")
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
    @GetMapping("/stores")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get stores",
            description = """
                    This endpoint is used to get stores. \s
                    Endpoint is used to get stores by search filters. \s
                    
                    Request parameters: \s
                        - nm: name of store \s
                        - strC: store code \s
                        - dsc: description \s
                        - sts: status \s
                    
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN can get stores. \s
                    """
    )
    public ResponseEntity<ApiResponse<List<StoreInfoResponse>>> getStores(
            @RequestParam(required = false, name = "nm") String name,
            @RequestParam(required = false, name = "strC") String storeCode,
            @RequestParam(required = false, name = "dsc") String description,
            @RequestParam(required = false, name = "sts") String status,
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
                    Endpoint is used to get store workers by search filters. \s
                    
                    Request parameters: \s
                    
                        - nm: name of worker \s
                        - snm: surname of worker \s
                        - usm: username of worker \s
                        - eml: email of worker \s
                        - phn: phone number of worker \s
                        - strId: store id of worker \s
                    
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role ADMIN or MANAGER can get store workers. \s
                    """
    )
    public ResponseEntity<ApiResponse<StoreDetailsResponse>> getStoreWorkers(
            @RequestParam(required = false, name = "strId") String storeId,
            @RequestParam(required = false, name = "nm") String name,
            @RequestParam(required = false, name = "snm") String surname,
            @RequestParam(required = false, name = "usm") String username,
            @RequestParam(required = false, name = "eml") String email,
            @RequestParam(required = false, name = "phn") String phone,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Worker authenticatedPrincipal
            ) {
        String id = storeId == null ? authenticatedPrincipal.getStoreId() : storeId;
        return ResponseEntity.ok(storeService.getStoreWorkers(id, name, surname, username, email, phone, page, size));
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
