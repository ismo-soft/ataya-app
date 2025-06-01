package com.ataya.contributor.repo;

import com.ataya.contributor.model.ShoppingCartMovement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingCartMovementRepository extends MongoRepository<ShoppingCartMovement, String> {
}
