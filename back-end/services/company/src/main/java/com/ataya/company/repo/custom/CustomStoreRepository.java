package com.ataya.company.repo.custom;

import com.ataya.company.model.Store;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomStoreRepository {

    List<Store> findStoresByCriteria(Query query);

    long countBookByCriteria(Query query);
}
