package com.ataya.inventory.repository;

import com.ataya.inventory.model.ProductItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductItemRepository extends MongoRepository<ProductItem, String> {
}
