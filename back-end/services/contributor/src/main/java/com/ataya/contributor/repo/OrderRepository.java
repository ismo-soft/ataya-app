package com.ataya.contributor.repo;

import com.ataya.contributor.enums.OrderStatus;
import com.ataya.contributor.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findByBuyerIdAndStatus(String id, OrderStatus orderStatus);
}
