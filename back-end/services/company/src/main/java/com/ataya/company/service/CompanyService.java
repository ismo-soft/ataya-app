package com.ataya.company.service;

import com.ataya.company.dto.company.CompanyDetailsResponse;
import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.company.UpdateCompanyRequest;
import com.ataya.company.util.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CompanyService {
    ApiResponse<CompanyInfoResponse> createCompany(CreateCompanyRequest createCompanyRequest, String OwnerId);

    ApiResponse<CompanyInfoResponse> getCompanyInfo(String companyId);

    ApiResponse<CompanyInfoResponse> updateCompany(String companyId, UpdateCompanyRequest updateCompanyRequest, MultipartFile logo, MultipartFile coverPhoto, MultipartFile profilePhoto);

    ApiResponse<CompanyDetailsResponse> getCompanyDetails(String companyId);

    ApiResponse<CompanyInfoResponse> setCompanyAddressId(String companyId, String addressId);

    CompanyInfoResponse getCompany(String companyId);

    boolean isStoreInCompany(String storeId, String companyId);
}
