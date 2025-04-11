package com.ataya.company.repo;

import com.ataya.company.enums.Role;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.custom.CustomWorkerRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends MongoRepository<Worker, String>, CustomWorkerRepository {
    Optional<Worker> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);

    Optional<Worker> findByEmail(String email);

    Optional<Worker> findByEmailVerificationToken(String phone);

}
