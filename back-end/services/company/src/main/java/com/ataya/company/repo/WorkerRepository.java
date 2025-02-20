package com.ataya.company.repo;

import com.ataya.company.model.Worker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface WorkerRepository extends MongoRepository<Worker, String> {
    Optional<Worker> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query(value = "{ '_id' : ?0 }", fields = "{ 'storeId' : 1, '_id' : 0 }")
    Optional<String> getStoreIdOfWorker(String userId);
}
