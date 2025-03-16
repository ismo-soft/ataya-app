package com.ataya.company.service;

import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.model.Store;
import com.ataya.company.util.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface StoreService {
    Store getStoreById(String storeId);

    ApiResponse<StoreInfoResponse> createStore(@Valid CreateStoreRequest createStoreRequest, String companyId);

    ApiResponse<StoreInfoResponse> getStoreInfo(String storeId);

    ApiResponse<List<StoreInfoResponse>> getStores(String name, String storeCode, String description, String status, int page, int size, String companyId);

    ApiResponse<StoreInfoResponse> updateStore(String storeId, @Valid UpdateStoreRequest updateStoreRequest);
}
