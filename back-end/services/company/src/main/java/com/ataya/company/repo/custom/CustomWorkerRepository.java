package com.ataya.company.repo.custom;

import com.ataya.company.model.Worker;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface CustomWorkerRepository {
    List<Worker> findWorkersByQuery(Query query);

    long countWorkersByQuery(Query query);
}
