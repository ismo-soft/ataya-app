package com.ataya.company.service;

import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.company.UpdateCompanyRequest;
import com.ataya.company.util.ApiResponse;

public interface CompanyService {
    ApiResponse createCompany(CreateCompanyRequest createCompanyRequest, String CEOid);

    ApiResponse getCompanyDetails(String companyId);

    ApiResponse updateCompany(String companyId, UpdateCompanyRequest updateCompanyRequest);
}
