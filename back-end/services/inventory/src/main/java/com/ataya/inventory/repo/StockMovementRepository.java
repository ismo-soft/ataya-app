package com.ataya.inventory.repo;

import com.ataya.inventory.model.StockMovement;
import com.ataya.inventory.repo.custom.CustomStockMovementRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockMovementRepository extends MongoRepository<StockMovement, String>, CustomStockMovementRepository {

}
