package com.ataya.inventory.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockMovementRepository extends MongoRepository<StockMovementRepository, String> {

}
