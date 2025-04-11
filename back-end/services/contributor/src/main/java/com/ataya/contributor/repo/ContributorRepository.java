package com.ataya.contributor.repo;

import com.ataya.contributor.model.Contributor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ContributorRepository extends MongoRepository<Contributor, Integer> {
    Optional<Contributor> findByEmail(String email);
}
