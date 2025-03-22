package com.ataya.company.controller;

import com.ataya.company.model.Worker;
import com.ataya.company.service.InventoryService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // add product quantity to store inventory
//    @PostMapping("/add")
//    @Operation(
//            summary = "Add product to inventory",
//            description = """
//                    Add product to inventory. \s
//                    ### Authentication: bearer token is required. \s
//                    ### Authorizations: user with role SUPER_ADMIN can add product to inventory. \s
//                    """
//    )
//    @PreAuthorize("@ProductSecurity.hasPermission(authentication.principal.companyId, #request.productId)")
//    public ResponseEntity<ApiResponse<ProductQuantityResponse>> addProduct(@RequestBody AddQuantityRequest request) {
//        return ResponseEntity.status(201).body(inventoryService.addQuantity(request));
//    }
}
