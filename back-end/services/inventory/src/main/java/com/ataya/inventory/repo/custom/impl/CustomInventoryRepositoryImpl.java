package com.ataya.inventory.repo.custom.impl;

import com.ataya.inventory.model.Inventory;
import com.ataya.inventory.repo.custom.CustomInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class CustomInventoryRepositoryImpl implements CustomInventoryRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Inventory> findInventoryByQuery(Query query) {
        return mongoTemplate.find(query, Inventory.class);
    }

    @Override
    public long countInventoryByQuery(Query query) {
        return mongoTemplate.count(query, Inventory.class);
    }
}
