package com.ataya.company.repo;

import com.ataya.company.model.Store;
import com.ataya.company.repo.custom.CustomStoreRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StoreRepository extends MongoRepository<Store, String>, CustomStoreRepository {
    List<Store> findAllByCompanyId(String companyId);

    boolean existsByIdAndCompanyId(String id, String companyId);
}
