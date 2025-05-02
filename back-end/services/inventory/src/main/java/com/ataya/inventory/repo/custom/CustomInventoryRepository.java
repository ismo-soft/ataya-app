package com.ataya.inventory.repo.custom;

import com.ataya.inventory.model.Inventory;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomInventoryRepository {

    List<Inventory> findInventoryByQuery(Query query);
    long countInventoryByQuery(Query query);
}
