package com.ataya.company.repo;

import com.ataya.company.model.BankAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BankAccountRepository extends MongoRepository<BankAccount, String> {
    boolean existsByStoreId(String storeId);

    List<BankAccount> findAllByStoreId(String storeId);
}
