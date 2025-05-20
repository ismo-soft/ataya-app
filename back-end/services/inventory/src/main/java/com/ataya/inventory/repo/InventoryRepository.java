package com.ataya.inventory.repo;

import com.ataya.inventory.model.Inventory;
import com.ataya.inventory.repo.custom.CustomInventoryRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface InventoryRepository extends MongoRepository<Inventory, String>, CustomInventoryRepository {
    Optional<Inventory> findByProductIdAndStoreId(String productId, String storeId);

    List<Inventory> findByProductIdInAndStoreId(Set<String> strings, String storeId);

    Optional<Inventory> findByIdAndStoreId(String inventoryId, String storeId);

    List<Inventory> findByStoreId(String storeId);

    Integer countAllByStoreId(String storeId);

    Integer countAllByStoreIdAndQuantityGreaterThan(String storeId, Double quantityIsGreaterThan);

    Integer countAllByStoreIdAndQuantity(String storeId, Double quantity);

    Integer countAllInventoriesByStoreIdAndIsDiscounted(String storeId, Boolean isDiscounted);

    @Query(value = "{'storeId': ?0, '$expr': { '$and': [ { '$gt': ['$reorderLevel', '$quantity'] }, { '$gt': ['$quantity', 0] } ] }}", count = true)
    Integer countWhereReorderLevelIsGreaterThanQuantity(String storeId);
}
