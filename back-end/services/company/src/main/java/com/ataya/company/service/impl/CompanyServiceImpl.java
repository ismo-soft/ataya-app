package com.ataya.company.service.impl;

import com.ataya.company.dto.company.CompanyDetailsResponse;
import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.company.UpdateCompanyRequest;
import com.ataya.company.dto.store.response.StoreResponse;
import com.ataya.company.dto.worker.response.WorkerDetailsResponse;
import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.exception.Custom.DuplicateResourceException;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.exception.Custom.ValidationException;
import com.ataya.company.mapper.CompanyMapper;
import com.ataya.company.model.Company;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.CompanyRepository;
import com.ataya.company.service.CompanyService;
import com.ataya.company.service.StoreService;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private WorkerService workerService;

    @Autowired
    @Lazy
    private StoreService storeService;

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
                        () -> new ResourceNotFoundException(
                                "Company",
                                "id",
                                companyId,
                                "Company not found with id: " + companyId + "."
                        )
                );
        // check if email already exists
        if (updateCompanyRequest.getEmail() != null && companyRepository.existsByEmail(updateCompanyRequest.getEmail())) {
            throw new DuplicateResourceException("Company", "email", updateCompanyRequest.getEmail(), null);
        }
        // check if phone number already exists
        if (updateCompanyRequest.getPhoneNumber() != null && companyRepository.existsByPhoneNumbwer(updateCompanyRequest.getPhoneNumber())) {
            throw new DuplicateResourceException("Company", "phoneNumber", updateCompanyRequest.getPhoneNumber(), null);
        }
        // check if registration number already exists
        if (updateCompanyRequest.getRegistrationNumber() != null && companyRepository.existsByRegistrationNumber(updateCompanyRequest.getRegistrationNumber())) {
            throw new DuplicateResourceException("Company", "registrationNumber", updateCompanyRequest.getRegistrationNumber(), null);
        }
        if (updateCompanyRequest.getDateOfIncorporation() != null && updateCompanyRequest.getDateOfIncorporation().isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "dateOfIncorporation",
                    updateCompanyRequest.getDateOfIncorporation(),
                    "Date of incorporation cannot be in the past"
            );
        }
        // check if social media platforms are valid
        if (updateCompanyRequest.getSocialMedia() != null) {
            for (SocialMediaPlatforms socialMedia : updateCompanyRequest.getSocialMedia().keySet()) {
                if (!SocialMediaPlatforms.isPlatformExists(socialMedia)) {
                    throw new ValidationException(
                            "socialMedia",
                            socialMedia,
                            "Invalid social media platform"
                    );
                }
            }
        }
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

    @Override
    public ApiResponse viewAllStores(String companyId) {
        // check if the company exists
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException(
                    "Company",
                    "id",
                    companyId,
                    "Company not found with id: " + companyId + "."
            );
        }
        // get all stores of the company
        List<Object> stores = storeService.getAllStoresOfCompany(companyId);
        return ApiResponse.builder()
                .status("OK")
                .message("Stores retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(stores)
                .statusCode(200)
                .build();
    }

    @Override
    public ApiResponse viewAllWorkersOfCompany(String companyId) {
        // check if the company exists
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException(
                    "Company",
                    "id",
                    companyId,
                    "Company not found with id: " + companyId + "."
            );
        }
        // get all workers of the company
        List<Object> workers = workerService.getAllWorkersOfCompany(companyId);
        return ApiResponse.builder()
                .status("OK")
                .message("Workers retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(workers)
                .statusCode(200)
                .build();

    }

    @Override
    public ApiResponse viewAllManagersOfCompany(String companyId) {
        // check if the company exists
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException(
                    "Company",
                    "id",
                    companyId,
                    "Company not found with id: " + companyId + "."
            );
        }
        // get all managers of the company
        List<Object> managers = workerService.getAllManagersOfCompany(companyId);
        return ApiResponse.builder()
                .status("OK")
                .message("Managers retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(managers)
                .statusCode(200)
                .build();
    }

    @Override
    public ApiResponse viewCompanyDetails(String companyId) {
        // get company
        Company company = companyRepository.findById(companyId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Company",
                                "id",
                                companyId,
                                "Company not found with id: " + companyId + "."
                        )
                );
        // get company stores
        List<Object> stores = storeService.getAllStoresOfCompany(companyId);

        // get company workers
        List<Object> workers = workerService.getAllWorkersOfCompany(companyId);
        return ApiResponse.builder()
                .status("OK")
                .message("Company details retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(
                        Map.of(
                                "company", companyMapper.companyToCompanyDto(company, CompanyDetailsResponse.class),
                                "stores", stores,
                                "workers", workers
                        )
                )
                .statusCode(200)
                .build();


    }

    @Override
    public ApiResponse setCompanyAddressId(String companyId, String addressId) {
        // get company
        Company company = companyRepository.findById(companyId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Company",
                                "id",
                                companyId,
                                "Company not found with id: " + companyId + "."
                        )
                );
        // set company address id
        company.setAddressId(addressId);
        companyRepository.save(company);
        return ApiResponse.builder()
                .status("OK")
                .message("Company address id set successfully")
                .timestamp(LocalDateTime.now())
                .data(
                        companyMapper.companyToCompanyDto(company, CompanyDetailsResponse.class)
                )
                .statusCode(200)
                .build();
    }

    @Override
    public boolean existsById(String companyId) {
        return companyRepository.existsById(companyId);
    }

    @Override
    public List<Object> getStoreWorkers(String companyId, String storeId) {
        return workerService.getStoreWorkers(companyId, storeId);
    }

    @Override
    public List<Object> getStoreManagers(String companyId, String storeId) {
        return workerService.getStoreManagers(companyId, storeId);
    }

    @Override
    public boolean isWorkerBelongToStoreAndManager(String storeId, String workerId) {
        if (storeId == null || workerId == null) {
            return false;
        }
        return workerService.isStoreWorker(storeId, workerId) && workerService.hasRole(workerId, "ROLE_MANAGER");
    }
}
