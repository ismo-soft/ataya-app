package com.ataya.company.repo;

import com.ataya.company.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StoreRepository extends MongoRepository<Store, String> {
    List<Store> findAllByCompanyId(String companyId);

    boolean existsByIdAndCompanyId(String id, String companyId);
}
