package com.ataya.company.service;

import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreResponse;
import com.ataya.company.util.ApiResponse;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    boolean existsById(String storeId);

    String getCompanyId(String storeId);

    String getManagerId(String storeId);

    List<Object> getAllStoresOfCompany(String companyId);

    boolean isStoreBelongToCompany(String storeId, String companyId);

    ApiResponse createStore(CreateStoreRequest createStoreRequest, String companyId);

    ApiResponse getStoreDetails(String storeId);

    ApiResponse updateStore(String storeId, UpdateStoreRequest updateStoreRequest);

    ApiResponse getAllWorkersOfStore(String storeId);

    ApiResponse getAllManagersOfStore(String storeId);

    ApiResponse setStoreManager(String storeId, String managerId);

    ApiResponse setStoreStatus(String storeId, String storeStatus);

    ApiResponse getStoreProfile(String storeId);

    ApiResponse updateStoreAddress(String storeId, String addressId);
}
