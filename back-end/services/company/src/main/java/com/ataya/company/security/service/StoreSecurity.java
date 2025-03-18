package com.ataya.company.security.service;

import com.ataya.company.exception.custom.InvalidOperationException;
import com.ataya.company.service.CompanyService;
import org.springframework.stereotype.Service;

@Service
public class StoreSecurity {

    private final CompanyService companyService;

    public StoreSecurity(CompanyService companyService) {
        this.companyService = companyService;
    }

    public boolean hasAccess(String storeId, String companyId) {
        if (companyId == null) {
            throw new InvalidOperationException(
                    "have Access",
                    "companyId"
            );
        }
        return companyService.isStoreInCompany(storeId, companyId);
    }
}
