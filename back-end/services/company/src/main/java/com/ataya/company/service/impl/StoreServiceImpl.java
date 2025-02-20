package com.ataya.company.service.impl;

import com.ataya.company.model.Store;
import com.ataya.company.repo.StoreRepository;
import com.ataya.company.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public boolean existsById(String storeId) {
        return storeRepository.existsById(storeId);
    }

    @Override
    public String getCompanyId(String storeId) {
        return storeRepository.findById(storeId).orElse(new Store()).getCompanyId();
    }

    @Override
    public String getManagerId(String storeId) {
        return storeRepository.findById(storeId).orElse(new Store()).getManagerId();
    }
}
