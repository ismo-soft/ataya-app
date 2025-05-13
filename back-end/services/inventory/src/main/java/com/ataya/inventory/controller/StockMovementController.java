package com.ataya.inventory.controller;

import com.ataya.inventory.model.StockMovement;
import com.ataya.inventory.service.StockMovementService;
import com.ataya.inventory.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint we need for this controller:
 * - POST: Register supply movement
 * - PUT: Edit Inventory quantity
 * - PUT: Transfer Inventory
 * - GET: Get all movements
 * - GET: Get movement by status
 */

@RestController
@RequestMapping("/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping("/supply")
    @Operation(
            summary = "Register supply movement",
            description = """
            This endpoint is used to register a supply movement in the inventory system.
            
            ### Authentication: \t Token is required \s
            ### Authorization: \t No Authority for this endpoint (user with any role can access this endpoint) \s
            
            """
    )
    public ResponseEntity<ApiResponse<StockMovementInfo>> supplyMovement(SupplyRequest supplyRequest) {
        return ResponseEntity.ok(
                stockMovementService.setSuppliement(supplyRequest)
        );
    }

    @PutMapping("/edit")
    @Operation(
            summary = "Edit Inventory quantity",
            description = """
            This endpoint is used to edit the inventory quantity in the inventory system.
            
            ### Authentication: \t Token is required \s
            ### Authorization: \t No Authority for this endpoint (user with any role can access this endpoint) \s
            
            """
    )
    public ResponseEntity<ApiResponse<StockMovementInfo>> editInventoryQuantity(List<EditQuantity> editQuantityList) {
        return ResponseEntity.ok(
                stockMovementService.editInventoryQuantity(editQuantityList)
        );
    }
}
