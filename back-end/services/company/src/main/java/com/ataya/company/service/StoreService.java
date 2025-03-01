package com.ataya.company.service;

import com.ataya.company.dto.store.response.StoreResponse;

import java.util.List;
import java.util.Optional;

public interface StoreService {
    boolean existsById(String storeId);

    String getCompanyId(String storeId);

    String getManagerId(String storeId);

    List<Object> getAllStoresOfCompany(String companyId);

    boolean isStoreBelontToCompany(String storeId, String companyId);
}
