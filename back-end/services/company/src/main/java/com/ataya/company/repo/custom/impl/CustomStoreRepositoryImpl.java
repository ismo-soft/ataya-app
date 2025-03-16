package com.ataya.company.repo.custom.impl;

import com.ataya.company.model.Store;
import com.ataya.company.repo.custom.CustomStoreRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class CustomStoreRepositoryImpl implements CustomStoreRepository {

    private final MongoTemplate mongoTemplate;

    public CustomStoreRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Store> findStoresByCriteria(Query query) {
        return mongoTemplate.find(query, Store.class);
    }

    @Override
    public long countBookByCriteria(Query query) {
        return mongoTemplate.count(query, Store.class);
    }


}
