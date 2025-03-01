package com.ataya.company.service.impl;

import com.ataya.company.dto.store.response.StoreResponse;
import com.ataya.company.mapper.StoreMapper;
import com.ataya.company.model.Store;
import com.ataya.company.repo.StoreRepository;
import com.ataya.company.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreMapper storeMapper;

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

    @Override
    public List<Object> getAllStoresOfCompany(String companyId) {
        // get all stores of the company
        List<Store> stores = storeRepository.findAllByCompanyId(companyId);
        // convert the stores to store response
        List<Object> storeResponses = new ArrayList<>();
        for (Store store : stores) {
            storeResponses.add(
                    storeMapper.storeToStoreDto(store, StoreResponse.class)
            );
        }
        return storeResponses;
    }

    @Override
    public boolean isStoreBelontToCompany(String storeId, String companyId) {
        return storeRepository.existsByIdAndCompanyId(storeId, companyId);
    }
}
