package com.ataya.inventory.service;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.dto.UpdateInventoryRequest;
import com.ataya.inventory.dto.contributor.CartItemStatistics;
import com.ataya.inventory.dto.stockMovement.EditQuantityRequest;
import com.ataya.inventory.dto.stockMovement.SupplyRequest;
import com.ataya.inventory.model.User;
import com.ataya.inventory.util.ApiResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InventoryService {
    ApiResponse<List<InventoryItemInfo>> getFilteredInventoryItems(String quantity, String price, String discountedPrice ,String discount, String discountRate, String storeId, String companyId, String productId, Integer page, Integer size);

    ApiResponse<InventoryItemInfo> updateInventoryItem(UpdateInventoryRequest requestBody, User user, String productId, String storeId);

    ApiResponse<List<InventoryItemInfo>> updateProductPrice(Map<String, Double> prdIdPriceMap, User user, String storeId);

    ApiResponse<List<InventoryItemInfo>> raiseProductPrice(Map<String, Double> prdIdPercentageMap, User user, String storeId);

    ApiResponse<List<InventoryItemInfo>> raiseAllProductPrice(Set<String> prdIds, User user, String storeId, double percentage);

    ApiResponse<List<InventoryItemInfo>> setDiscount(Map<String, Double> prdIdDiscountMap, User user, String storeId);

    ApiResponse<List<InventoryItemInfo>> setDiscountRate(Map<String, Double> prdIdDiscountRateMap, User user, String storeId);

    ApiResponse<List<InventoryItemInfo>> setSameDiscountRate(Set<String> prdIds, User user, String storeId, String percentage);

    ApiResponse<InventoryItemInfo> editInventoryItemQuantity(EditQuantityRequest request, User user);

    ApiResponse<List<InventoryItemInfo>> supplyInventoryItems(SupplyRequest request, User user);

    void createInventoriesForNotExistProducts(Map<String, Double> prdIdQuantityMap, String reason, String note, String companyId, String storeId, String user);

    void suspendItem(String itemId, Double quantity);

    void releaseSuspendedItem(String itemId, Double quantity);

    void releaseSuspendedForSoldItems(CartItemStatistics itemRequest);
}
