package com.ataya.company.service;

import com.ataya.company.dto.store.StoreDto;
import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreDetailsResponse;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.model.Store;
import com.ataya.company.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreService {
    Store getStoreById(String storeId);

    ApiResponse<StoreInfoResponse> createStore(@Valid CreateStoreRequest createStoreRequest, String companyId, MultipartFile profilePhoto);

    ApiResponse<StoreInfoResponse> getStoreInfo(String storeId);

    ApiResponse<List<StoreInfoResponse>> getStores(String name, String storeCode, String description, String status, int page, int size, String companyId);

    ApiResponse<StoreInfoResponse> updateStore(String storeId, @Valid UpdateStoreRequest updateStoreRequest, MultipartFile profilePhoto);

    boolean isStoreOfCompany(String storeId, String companyId);

    ApiResponse<StoreDetailsResponse> getStoreWorkers(String storeId, String name, String surname, String username, String email, String phone, int page, int size);

    ApiResponse<StoreDetailsResponse> getStoreDetails(String storeId);

    void createStoreWithDefaults(String companyId);

    List<StoreDto> getAllStoresAsDto();

    StoreDto getStoreByIdAsDto(String storeId);
}
