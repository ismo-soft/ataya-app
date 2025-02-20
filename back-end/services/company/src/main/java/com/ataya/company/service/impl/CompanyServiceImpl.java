package com.ataya.company.service.impl;

import com.ataya.company.dto.company.CompanyDetailsResponse;
import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.company.UpdateCompanyRequest;
import com.ataya.company.exception.Custom.DuplicateResourceException;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.mapper.CompanyMapper;
import com.ataya.company.model.Company;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.CompanyRepository;
import com.ataya.company.service.CompanyService;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private WorkerService workerService;

    @Override
    public ApiResponse createCompany(CreateCompanyRequest createCompanyRequest, String CEOid) {
        // check if the company already exists
        if (companyRepository.existsByRegistrationNumber(createCompanyRequest.getRegistrationNumber())) {
            throw new DuplicateResourceException("Company", "registrationNumber", createCompanyRequest.getRegistrationNumber(), null);
        }
        // create the company
        Company company = Company.builder()
                .name(createCompanyRequest.getName())
                .enrollmentDate(LocalDate.now())
                .registrationNumber(createCompanyRequest.getRegistrationNumber())
                .ceoId(CEOid)
                .build();
        Company saved = companyRepository.save(company);
        workerService.setCompany(CEOid, saved.getId());
        return ApiResponse.builder()
                .status("Created")
                .message("Company created successfully")
                .timestamp(LocalDateTime.now())
                .data(
                        companyMapper.companyToCompanyDto(company, CompanyDetailsResponse.class)
                )
                .statusCode(201)
                .build();
    }

    @Override
    public ApiResponse getCompanyDetails(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(
                        () -> new DuplicateResourceException("Company", "id", companyId, null)
                );
        return ApiResponse.builder()
                .status("OK")
                .message("Company retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(
                        companyMapper.companyToCompanyDto(company, CompanyDetailsResponse.class)
                )
                .statusCode(200)
                .build();
    }

    @Override
    public ApiResponse updateCompany(String companyId, UpdateCompanyRequest updateCompanyRequest) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(
                        () -> new DuplicateResourceException("Company", "id", companyId, null)
                );
        company = companyMapper.CompanyDtoToCompany(company, updateCompanyRequest, UpdateCompanyRequest.class);
        companyRepository.save(company);
        return ApiResponse.builder()
                .status("OK")
                .message("Company updated successfully")
                .timestamp(LocalDateTime.now())
                .data(
                        companyMapper.companyToCompanyDto(company, CompanyDetailsResponse.class)
                )
                .statusCode(200)
                .build();
    }
}
