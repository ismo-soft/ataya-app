package com.ataya.inventory.controller;

/*
 * Endpoints:
 * endpoint to get all items of store
 * endpoint to get all items of store by search filters
 * endpoint ot create item
 * endpoint to update item
 */

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @PostMapping("/create/{storeId}/{productId}")
    @Operation(
            summary = "Create Inventory Item",
            description = """
            This endpoint creates a new inventory item for a specific store and product.
            It requires the store ID and product ID as path parameters.
            Cannot be used to create an item for a product that already exists in the store.
            Item will be created with default values.
            """
    )
//    public ResponseEntity<ApiResponse<InventoryInfoResponse>> createInventoryItem(
//            @PathVariable String storeId,
//            @PathVariable String productId,
//            @RequestBody CreateInventoryRequest createInventoryRequest
//    ) {
//
//    }
    public String testBeforeCoding(@AuthenticationPrincipal String username) {
        return "Hello " + username;
    }

}
