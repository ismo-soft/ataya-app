package com.ataya.company.repo;

import com.ataya.company.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {
    boolean existsByRegistrationNumber(String registrationNumber);

    boolean existsByIdAndCeoId(String id, String ceoId);
}
