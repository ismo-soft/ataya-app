package com.ataya.company.repo.custom.impl;

import com.ataya.company.model.Worker;
import com.ataya.company.repo.custom.CustomWorkerRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class CustomWorkerRepositoryImpl implements CustomWorkerRepository {

    private final MongoTemplate mongoTemplate;

    public CustomWorkerRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Worker> findWorkersByQuery(Query query) {
        return mongoTemplate.find(query, Worker.class);
    }

    @Override
    public long countWorkersByQuery(Query query) {
        return mongoTemplate.count(query, Worker.class);
    }
}
