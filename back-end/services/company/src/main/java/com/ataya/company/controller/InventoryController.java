package com.ataya.company.controller;

import com.ataya.company.service.InventoryService;
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
//    public ResponseEntity<ApiResponse<ProductQuantityResponse>> addProduct(@RequestBody AddQuantityRequest request) {
//        return ResponseEntity.status(201).body(inventoryService.addQuantity(request));
//    }
}
