package com.ataya.inventory.service;

import com.ataya.inventory.dto.InventoryItemInfo;
import com.ataya.inventory.util.ApiResponse;

import java.util.List;

public interface InventoryService {
    ApiResponse<List<InventoryItemInfo>> getFilteredInventoryItems(String quantity, String price, String discount, String discountRate, String storeId, String companyId, String productId, int page, int size);
}
