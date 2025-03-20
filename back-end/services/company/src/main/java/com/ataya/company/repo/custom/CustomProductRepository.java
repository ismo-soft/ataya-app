package com.ataya.company.repo.custom;

import com.ataya.company.model.Product;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomProductRepository {

    List<Product> findProductsByQuery(Query query);

    long countProductByQuery(Query query);
}
