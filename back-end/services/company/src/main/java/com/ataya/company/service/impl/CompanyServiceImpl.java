package com.ataya.company.service.impl;

import com.ataya.company.dto.company.CompanyDetailsResponse;
import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.company.UpdateCompanyRequest;
import com.ataya.company.dto.product.ProductInfoResponse;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.exception.custom.DuplicateResourceException;
import com.ataya.company.exception.custom.ResourceNotFoundException;
import com.ataya.company.mapper.CompanyMapper;
import com.ataya.company.model.Company;
import com.ataya.company.model.Store;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.CompanyRepository;
import com.ataya.company.service.*;
import com.ataya.company.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    private final WorkerService workerService;

    private final StoreService storeService;

    private final FileService fileService;

    private final ProductService productService;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper, WorkerService workerService, StoreService storeService, FileService fileService, ProductService productService) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.workerService = workerService;
        this.storeService = storeService;
        this.fileService = fileService;
        this.productService = productService;
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
        // check if owner companyId is null
        if (owner.getCompanyId() != null) {
            throw new DuplicateResourceException(
                    "Worker",
                    "companyId",
                    owner.getCompanyId(),
                    "Worker with username " + owner.getUsername() + " already has a company"
            );
        }
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
    public ApiResponse<CompanyInfoResponse> updateCompany(String companyId, UpdateCompanyRequest updateCompanyRequest, MultipartFile logo, MultipartFile coverPhoto, MultipartFile profilePhoto) {
        // Get company by companyId
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Company",
                        "id",
                        companyId,
                        "Company not found with id " + companyId
                )
        );

        Map<SocialMediaPlatforms, String> socialMedia = new HashMap<>();
        Map<String, String> socialMediaString = updateCompanyRequest.getSocialMedia();
        // validate social media of update company request and copy them in socialMedia map
        for (Map.Entry<String, String> entry : socialMediaString.entrySet()) {
            if (SocialMediaPlatforms.isPlatformExists(entry.getKey())) {
                socialMedia.put(SocialMediaPlatforms.getPlatform(entry.getKey()), entry.getValue());
            } else {
                throw new ResourceNotFoundException(
                        "Social Media Platform",
                        "platform",
                        entry.getKey(),
                        "Social Media Platform " + entry.getKey() + " not found"
                );
            }
        }

        String logoPath = null;
        String coverPhotoPath = null;
        String profilePhotoPath = null;
        if (logo != null) {
            if (!logo.isEmpty()) {
                logoPath = fileService.saveImageFile(logo, "company", "logo", company.getId());
                company.setLogo(logoPath);
            }
        }
        if (coverPhoto != null) {
            if (!coverPhoto.isEmpty()) {
                coverPhotoPath = fileService.saveImageFile(coverPhoto, "company", "cover", company.getId());
                company.setCoverPhoto(coverPhotoPath);
            }
        }
        if (profilePhoto != null) {
            if (!profilePhoto.isEmpty()) {
                profilePhotoPath = fileService.saveImageFile(profilePhoto, "company", "profile", company.getId());
                company.setProfilePhoto(profilePhotoPath);
            }
        }

        // update company
        company.setName(updateCompanyRequest.getName());
        company.setPhoneNumber(updateCompanyRequest.getPhoneNumber());
        company.setEmail(updateCompanyRequest.getEmail());
        company.setAddress(updateCompanyRequest.getAddress());
        company.setDateOfIncorporation(updateCompanyRequest.getDateOfIncorporation());
        company.setWebsite(updateCompanyRequest.getWebsite());
        company.setLogo(logoPath);
        company.setDescription(updateCompanyRequest.getDescription());
        company.setCoverPhoto(coverPhotoPath);
        company.setProfilePhoto(profilePhotoPath);
        company.setSocialMedia(socialMedia);
        company.setLegalEntityType(updateCompanyRequest.getLegalEntityType());
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

        List<StoreInfoResponse> stores = storeService.getStores(
                null,
                null,
                null,
                null,
                0,
                0,
                companyId
        ).getData();
        List<WorkerInfoResponse> workers = workerService.getWorkers(
                null,
                null,
                null,
                null,
                null,
                companyId,
                null,
                false,
                0,
                0
        ).getData();
        List<ProductInfoResponse> products = productService.getProducts(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                0,
                companyId,
                null
        ).getData();

        return ApiResponse.<CompanyDetailsResponse>builder()
                .message("Company details retrieved successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        CompanyDetailsResponse.builder()
                        .company(companyMapper.toCompanyInfoResponse(company))
                        .workers(workers)
                        .stores(stores)
                        .products(products)
                        .productCount(products.size())
                        .storeCount(stores.size())
                        .workerCount(workers.size())
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
