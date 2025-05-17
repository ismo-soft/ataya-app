package com.ataya.inventory.service;

import com.ataya.inventory.dto.company.ProductDto;
import com.ataya.inventory.dto.stockMovement.EditQuantityRequest;
import com.ataya.inventory.dto.stockMovement.GetMovementsParameters;
import com.ataya.inventory.dto.stockMovement.MovementInfo;
import com.ataya.inventory.dto.stockMovement.SupplyRequest;
import com.ataya.inventory.model.User;
import com.ataya.inventory.util.ApiResponse;

import java.util.List;

public interface StockMovementService {

    void addSupplyMove(String inventoryId,double quantity, SupplyRequest request, User user);

    void addCreateInventoryMove(String inventoryId, ProductDto productDto, double quantity, String storeId);

    void editInventoryItemQuantity(EditQuantityRequest request,double quantityDifferent, String username);

    ApiResponse<List<MovementInfo>> getStockMovements(GetMovementsParameters parameters);

}
