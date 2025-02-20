package com.ataya.company.service;

import java.util.Optional;

public interface StoreService {
    boolean existsById(String storeId);

    String getCompanyId(String storeId);

    String getManagerId(String storeId);
}
