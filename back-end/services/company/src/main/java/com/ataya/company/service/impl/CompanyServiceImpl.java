package com.ataya.company.service.impl;

import com.ataya.company.dto.company.CompanyDetailsResponse;
import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.company.UpdateCompanyRequest;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.exception.Custom.DuplicateResourceException;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.mapper.CompanyMapper;
import com.ataya.company.model.Company;
import com.ataya.company.model.Store;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.CompanyRepository;
import com.ataya.company.service.CompanyService;
import com.ataya.company.service.StoreService;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final WorkerService workerService;

    private final StoreService storeService;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper, WorkerService workerService, StoreService storeService) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.workerService = workerService;
        this.storeService = storeService;
    }

    @Override
    public ApiResponse<CompanyInfoResponse> createCompany(CreateCompanyRequest createCompanyRequest, String ownerId) {
        // check if registration number is unique
        if (companyRepository.existsByRegistrationNumber(createCompanyRequest.getRegistrationNumber())) {
            throw new DuplicateResourceException(
                    "Company",
                    "registrationNumber",
                    createCompanyRequest.getRegistrationNumber(),
                    "Company with registration number " + createCompanyRequest.getRegistrationNumber() + " already exists"
            );
        }
        // get Worker by ownerId
        Worker owner = workerService.getWorkerById(ownerId);
        // create company
        Company company = Company.builder()
                .name(createCompanyRequest.getName())
                .registrationNumber(createCompanyRequest.getRegistrationNumber())
                .phoneNumber(owner.getPhoneNumber())
                .email(owner.getEmail())
                .description(createCompanyRequest.getName())
                .enrollmentDate(LocalDate.now())
                .ownerId(ownerId)
                .build();
        // save company
        company = companyRepository.save(company);
        // set companyId to user
        owner.setCompanyId(company.getId());
        // save user
        workerService.saveWorker(owner);
        return ApiResponse.<CompanyInfoResponse>builder()
                .message("Company created successfully")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(companyMapper.toCompanyInfoResponse(company))
                .build();
    }

    @Override
    public ApiResponse<CompanyInfoResponse> getCompanyInfo(String companyId) {
        // Get company by companyId
        return ApiResponse.<CompanyInfoResponse>builder()
                .message("Company retrieved successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(getCompany(companyId))
                .build();
    }

    @Override
    public ApiResponse<CompanyInfoResponse> updateCompany(String companyId, UpdateCompanyRequest updateCompanyRequest) {
        // Get company by companyId
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Company",
                        "id",
                        companyId,
                        "Company not found with id " + companyId
                )
        );
        // update company
        company.setName(updateCompanyRequest.getName());
        company.setPhoneNumber(updateCompanyRequest.getPhoneNumber());
        company.setEmail(updateCompanyRequest.getEmail());
        company.setAddress(updateCompanyRequest.getAddress());
        company.setDateOfIncorporation(updateCompanyRequest.getDateOfIncorporation());
        company.setWebsite(updateCompanyRequest.getWebsite());
        company.setLogo(updateCompanyRequest.getLogo());
        company.setDescription(updateCompanyRequest.getDescription());
        company.setCoverPhoto(updateCompanyRequest.getCoverPhoto());
        company.setProfilePhoto(updateCompanyRequest.getProfilePhoto());
        company.setSocialMedia(updateCompanyRequest.getSocialMedia());
        company.setLegalEntityType(updateCompanyRequest.getLegalEntityType());
        company.setRegistrationNumber(updateCompanyRequest.getRegistrationNumber());
        company.setTaxId(updateCompanyRequest.getTaxId());
        company.setCurrency(updateCompanyRequest.getCurrency());
        company.setIndustry(updateCompanyRequest.getIndustry());
        company.setSector(updateCompanyRequest.getSector());
        company.setAddressId(updateCompanyRequest.getAddressId());

        // save company
        company = companyRepository.save(company);
        return ApiResponse.<CompanyInfoResponse>builder()
                .message("Company updated successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(companyMapper.toCompanyInfoResponse(company))
                .build();
    }

    @Override
    public ApiResponse<CompanyDetailsResponse> getCompanyDetails(String companyId) {
        // Get company by companyId
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Company",
                        "id",
                        companyId,
                        "Company not found with id " + companyId
                )
        );
        // TODO: get company workers from worker service
//        List<WorkerInfoResponse> workers = workerService.getCompanyWorkers(companyId);
        // TODO: get company stores from store service
//        List<StoreInfoResponse> stores = storeService.getCompanyStores(companyId);

        return ApiResponse.<CompanyDetailsResponse>builder()
                .message("Company details retrieved successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        CompanyDetailsResponse.builder()
                        .company(companyMapper.toCompanyInfoResponse(company))
                        .workers(null)
                        .stores(null)
                        .build()
                )
                .build();

    }

    @Override
    public ApiResponse<CompanyInfoResponse> setCompanyAddressId(String companyId, String addressId) {
        // Get company by companyId
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Company",
                        "id",
                        companyId,
                        "Company not found with id " + companyId
                )
        );
        // set company addressId
        company.setAddressId(addressId);
        // save company
        company = companyRepository.save(company);
        return ApiResponse.<CompanyInfoResponse>builder()
                .message("Company addressId set successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(companyMapper.toCompanyInfoResponse(company))
                .build();
    }

    @Override
    public CompanyInfoResponse getCompany(String companyId) {
        if (companyId == null) {
            throw new ResourceNotFoundException(
                    "Company",
                    "id",
                    null,
                    "Company id is required"
            );
        }
        // Get company by companyId
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Company",
                        "id",
                        companyId,
                        "Company not found with id " + companyId
                )
        );
        return companyMapper.toCompanyInfoResponse(company);
    }

    @Override
    public boolean isStoreInCompany(String storeId, String companyId) {
        Store store = storeService.getStoreById(storeId);
        if (store == null) {
            return false;
        }
        return store.getCompanyId().equals(companyId);
    }
}
