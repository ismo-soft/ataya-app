package com.ataya.inventory.controller;

import com.ataya.inventory.dto.stockMovement.GetMovementsParameters;
import com.ataya.inventory.dto.stockMovement.MovementInfo;
import com.ataya.inventory.service.StockMovementService;
import com.ataya.inventory.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/movements")
    public ResponseEntity<ApiResponse<List<MovementInfo>>> getStockMovements(
            @RequestBody GetMovementsParameters parameters
    ) {
        return ResponseEntity.ok(
                stockMovementService.getStockMovements(parameters)
        );
    }

}
