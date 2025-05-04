package com.ataya.inventory.service;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.dto.UpdateInventoryRequest;
import com.ataya.inventory.dto.company.ProductDto;
import com.ataya.inventory.dto.company.StoreDto;
import com.ataya.inventory.model.User;
import com.ataya.inventory.util.ApiResponse;

import java.util.List;

public interface InventoryService {
    ApiResponse<List<InventoryItemInfo>> getFilteredInventoryItems(String quantity, String price, String discount, String discountRate, String storeId, String companyId, String productId, int page, int size);

    ApiResponse<InventoryItemInfo> updateInventoryItem(UpdateInventoryRequest requestBody, User user, String productId, String storeId);

    void createProductInventory(ProductDto productDto);

    void createStoreInventory(StoreDto storeDto);
}
