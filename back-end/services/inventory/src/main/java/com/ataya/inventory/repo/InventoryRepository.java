package com.ataya.inventory.repo;

import com.ataya.inventory.model.Inventory;
import com.ataya.inventory.repo.custom.CustomInventoryRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<Inventory, String>, CustomInventoryRepository {
}
