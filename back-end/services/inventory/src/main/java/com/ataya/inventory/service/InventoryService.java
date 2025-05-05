package com.ataya.inventory.service;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.dto.UpdateInventoryRequest;
import com.ataya.inventory.dto.company.ProductDto;
import com.ataya.inventory.dto.company.StoreDto;
import com.ataya.inventory.model.User;
import com.ataya.inventory.util.ApiResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InventoryService {
    ApiResponse<List<InventoryItemInfo>> getFilteredInventoryItems(String quantity, String price, String discountedPrice ,String discount, String discountRate, String storeId, String companyId, String productId, Integer page, Integer size);

    ApiResponse<InventoryItemInfo> updateInventoryItem(UpdateInventoryRequest requestBody, User user, String productId, String storeId);

    void createProductInventory(ProductDto productDto);

    void createStoreInventory(StoreDto storeDto);

    ApiResponse<List<InventoryItemInfo>> updateProductPrice(Map<String, Double> prdIdPriceMap, User user, String storeId);

    ApiResponse<List<InventoryItemInfo>> raiseProductPrice(Map<String, Double> prdIdPercentageMap, User user, String storeId);

    ApiResponse<List<InventoryItemInfo>> raiseAllProductPrice(Set<String> prdIds, User user, String storeId, double percentage);

    ApiResponse<List<InventoryItemInfo>> setDiscount(Map<String, Double> prdIdDiscountMap, User user, String storeId);

    ApiResponse<List<InventoryItemInfo>> setDiscountRate(Map<String, Double> prdIdDiscountRateMap, User user, String storeId);

    ApiResponse<List<InventoryItemInfo>> setSameDiscountRate(Set<String> prdIds, User user, String storeId, String percentage);
}
