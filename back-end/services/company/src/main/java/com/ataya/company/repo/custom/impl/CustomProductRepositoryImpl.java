package com.ataya.company.repo.custom.impl;

import com.ataya.company.model.Product;
import com.ataya.company.repo.custom.CustomProductRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final MongoTemplate mongoTemplate;

    public CustomProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Product> findProductsByQuery(Query query) {
        return mongoTemplate.find(query, Product.class);
    }

    @Override
    public long countProductByQuery(Query query) {
        return mongoTemplate.count(query, Product.class);
    }
}
