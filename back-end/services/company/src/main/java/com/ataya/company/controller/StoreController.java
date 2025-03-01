package com.ataya.company.controller;


import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.model.Worker;
import com.ataya.company.service.StoreService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company/store")
public class StoreController {
    /*
    * 1. Create a new store
    * 2. Get store information
    * 3. Update store information
    * 4. get all workers of store
    * 5. get all managers of store
    * 6. set store manager
    * 7. set store status
    * 8. get store details
    * 9. update address of store
    **/
    @Autowired
    private StoreService storeService;

    @PostMapping("/create")
    @Operation(
            summary = "Create a new store",
            description = "Create a new store with the given information"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createStore(@RequestBody CreateStoreRequest createStoreRequest, @AuthenticationPrincipal Worker authenticatedPrincipal) {
        return ResponseEntity.ok(storeService.createStore(createStoreRequest, authenticatedPrincipal.getCompanyId()));
    }

    @GetMapping("/profile/{storeId}")
    @Operation(
            summary = "Get store information",
            description = "Get store information with the given store id"
    )
    public ResponseEntity<ApiResponse> getStoreProfile(@PathVariable String storeId) {
        return ResponseEntity.ok(storeService.getStoreProfile(storeId));
    }

    @PutMapping("/profile/{storeId}")
    @Operation(
            summary = "Update store information",
            description = "Update store information with the given store id"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ADMIN') and" +
                    "@storeServiceImpl.isStoreBelongToCompany(#storeId, authentication.principal.companyId)" +
            ") or " +
            "(" +
                    "#storeId == authentication.principal.storeId and " +
                    "hasRole('MANAGER')" +
            ")"
    )
    public ResponseEntity<ApiResponse> updateStore(@PathVariable String storeId, @RequestBody UpdateStoreRequest updateStoreRequest) {
        return ResponseEntity.ok(storeService.updateStore(storeId, updateStoreRequest));
    }

    @GetMapping("/{storeId}/workers")
    @Operation(
            summary = "Get all workers of store",
            description = "Get all workers of store with the given store id"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ADMIN') and" +
                    "@storeServiceImpl.isStoreBelongToCompany(#storeId, authentication.principal.companyId)" +
            ") or " +
            "(" +
                    "#storeId == authentication.principal.storeId and " +
                    "hasRole('MANAGER')" +
            ")"
    )
    public ResponseEntity<ApiResponse> getAllWorkersOfStore(@PathVariable String storeId) {
        return ResponseEntity.ok(storeService.getAllWorkersOfStore(storeId));
    }

    @GetMapping("/{storeId}/managers")
    @Operation(
            summary = "Get all managers of store",
            description = "Get all managers of store with the given store id"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ADMIN') and" +
                    "@storeServiceImpl.isStoreBelongToCompany(#storeId, authentication.principal.companyId)" +
            ") or " +
            "(" +
                    "#storeId == authentication.principal.storeId and " +
                    "hasRole('MANAGER')" +
            ")"
    )
    public ResponseEntity<ApiResponse> getAllManagersOfStore(@PathVariable String storeId) {
        return ResponseEntity.ok(storeService.getAllManagersOfStore(storeId));
    }

    @PutMapping("/{storeId}/set-manager/{managerId}")
    @Operation(
            summary = "Set store manager",
            description = "Set store manager with the given store id and manager id"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ADMIN') and" +
                    "@storeServiceImpl.isStoreBelongToCompany(#storeId, authentication.principal.companyId)" +
            ")"
    )
    public ResponseEntity<ApiResponse> setStoreManager(@PathVariable String storeId, @PathVariable String managerId) {
        return ResponseEntity.ok(storeService.setStoreManager(storeId, managerId));
    }

    @PutMapping("/{storeId}/set-status")
    @Operation(
            summary = "Set store status",
            description = "Set store status with the given store id"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ADMIN') and" +
                    "@storeServiceImpl.isStoreBelongToCompany(#storeId, authentication.principal.companyId)" +
            ")"         
    )
    public ResponseEntity<ApiResponse> setStoreStatus(@PathVariable String storeId, @RequestParam String storeStatus) {
        return ResponseEntity.ok(storeService.setStoreStatus(storeId, storeStatus));
    }

    @GetMapping("/{storeId}")
    @Operation(
            summary = "Get store details",
            description = "Get store details with the given store id"
    )
    public ResponseEntity<ApiResponse> getStoreDetails(@PathVariable String storeId) {
        return ResponseEntity.ok(storeService.getStoreDetails(storeId));
    }

    @PutMapping("/{storeId}/address")
    @Operation(
            summary = "Update address of store",
            description = "Update address of store with the given store id"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ADMIN') and" +
                    "@storeServiceImpl.isStoreBelongToCompany(#storeId, authentication.principal.companyId)" +
            ")"
    )
    public ResponseEntity<ApiResponse> updateStoreAddress(@PathVariable String storeId, @RequestParam String addressId) {
        return ResponseEntity.ok(storeService.updateStoreAddress(storeId, addressId));
    }
}
