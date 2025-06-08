package com.ataya.contributor.repo;

import com.ataya.contributor.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
