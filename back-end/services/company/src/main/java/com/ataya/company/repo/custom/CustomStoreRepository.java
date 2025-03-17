package com.ataya.company.repo.custom;

import com.ataya.company.model.Store;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomStoreRepository {

    List<Store> findStoresByQuery(Query query);

    long countStoreByQuery(Query query);
}
