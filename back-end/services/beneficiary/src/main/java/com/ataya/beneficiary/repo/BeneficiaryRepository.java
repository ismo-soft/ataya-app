package com.ataya.beneficiary.repo;

import com.ataya.beneficiary.model.Beneficiary;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BeneficiaryRepository extends CrudRepository<Beneficiary, Long> {
    Optional<Beneficiary> findByIdentityNumber(String identityNumber);
}
