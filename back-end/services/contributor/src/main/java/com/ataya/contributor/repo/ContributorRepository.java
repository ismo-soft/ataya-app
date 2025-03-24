package com.ataya.contributor.repo;

import com.ataya.contributor.model.Contributor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ContributorRepository extends CrudRepository<Contributor, Integer> {
    Optional<Contributor> findByEmail(String email);
}
