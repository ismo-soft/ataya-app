package com.ataya.company.repo;

import com.ataya.company.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CompanyRepository extends MongoRepository<Company, String> {
    boolean existsByRegistrationNumber(String registrationNumber);
}
