package com.ataya.inventory.repo;

import com.ataya.inventory.model.Inventory;
import com.ataya.inventory.repo.custom.CustomInventoryRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InventoryRepository extends MongoRepository<Inventory, String>, CustomInventoryRepository {
    Optional<Inventory> findByProductIdAndStoreId(String productId, String storeId);
}
