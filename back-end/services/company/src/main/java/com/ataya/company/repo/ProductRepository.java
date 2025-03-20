package com.ataya.company.repo;

import com.ataya.company.model.Product;
import com.ataya.company.repo.custom.CustomProductRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String>, CustomProductRepository {

}
