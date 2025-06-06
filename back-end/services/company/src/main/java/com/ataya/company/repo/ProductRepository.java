package com.ataya.company.repo;

import com.ataya.company.model.Product;
import com.ataya.company.repo.custom.CustomProductRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProductRepository extends MongoRepository<Product, String>, CustomProductRepository {

    List<Product> findByCompanyId(String companyId);

    List<Product> findAllByIdInAndCompanyId(Collection<String> id, String companyId);
}
