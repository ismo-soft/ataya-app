package com.ataya.inventory.repo.custom;

import com.ataya.inventory.model.StockMovement;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomStockMovementRepository {
    List<StockMovement> findStockMovementsByQuery(Query query);
    long countStockMovementsByQuery(Query query);
}
