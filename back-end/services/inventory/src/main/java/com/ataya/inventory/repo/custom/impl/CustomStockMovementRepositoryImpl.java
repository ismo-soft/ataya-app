package com.ataya.inventory.repo.custom.impl;

import com.ataya.inventory.model.StockMovement;
import com.ataya.inventory.repo.custom.CustomStockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class CustomStockMovementRepositoryImpl implements CustomStockMovementRepository {

    private final MongoTemplate mongoTemplate;
    @Override
    public List<StockMovement> findStockMovementsByQuery(Query query) {
        return mongoTemplate.find(query, StockMovement.class);
    }

    @Override
    public long countStockMovementsByQuery(Query query) {
        return mongoTemplate.count(query, StockMovement.class);
    }
}
