package com.ataya.contributor.repo;

import com.ataya.contributor.model.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {

    Optional<ShoppingCart> findByCustomerId(String userId);
}
