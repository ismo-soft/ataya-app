package com.ataya.company.service.impl;

import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreResponse;
import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.enums.StoreStatus;
import com.ataya.company.exception.Custom.DuplicateResourceException;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.exception.Custom.ValidationException;
import com.ataya.company.mapper.StoreMapper;
import com.ataya.company.model.Store;
import com.ataya.company.repo.StoreRepository;
import com.ataya.company.service.CompanyService;
import com.ataya.company.service.StoreService;
import com.ataya.company.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    @Lazy
    private CompanyService companyService;

    @Override
    public boolean existsById(String storeId) {
        return storeRepository.existsById(storeId);
    }

    @Override
    public String getCompanyId(String storeId) {
        return storeRepository.findById(storeId).orElse(new Store()).getCompanyId();
    }

    @Override
    public String getManagerId(String storeId) {
        return storeRepository.findById(storeId).orElse(new Store()).getManagerId();
    }

    @Override
    public List<Object> getAllStoresOfCompany(String companyId) {
        // get all stores of the company
        List<Store> stores = storeRepository.findAllByCompanyId(companyId);
        // convert the stores to store response
        List<Object> storeResponses = new ArrayList<>();
        for (Store store : stores) {
            storeResponses.add(
                    storeMapper.storeToStoreDto(store, StoreResponse.class)
            );
        }
        return storeResponses;
    }

    @Override
    public boolean isStoreBelongToCompany(String storeId, String companyId) {
        return storeRepository.existsByIdAndCompanyId(storeId, companyId);
    }

    @Override
    public ApiResponse createStore(CreateStoreRequest createStoreRequest, String companyId) {
        // check if the company exists
        if (companyId != null) {
            if (!companyService.existsById(companyId)) {
                throw new ValidationException(
                        "company",
                        companyId,
                        "Company does not exist"
                );
            }
        }
        else {
            throw new ValidationException(
                    "company",
                    companyId,
                    "Company does not exist"
            );
        }
        // validate email address
        if (createStoreRequest.getEmail() != null) {
            if (!storeRepository.existsByIdAndCompanyId(createStoreRequest.getEmail(), companyId)) {
                throw new DuplicateResourceException(
                        "Store",
                        "email",
                        createStoreRequest.getEmail(),
                        "Email already exists"
                );
            }
        }
        // validate phone number
        if (createStoreRequest.getPhoneNumber() != null) {
            if (!storeRepository.existsByIdAndCompanyId(createStoreRequest.getPhoneNumber(), companyId)) {
                throw  new DuplicateResourceException(
                        "Store",
                        "phone number",
                        createStoreRequest.getPhoneNumber(),
                        "Phone number already exists"
                );
            }
        }
        // validate social media links
        createStoreRequest.getSocialMedia().keySet().forEach(
                socialMedia -> {
                    if(!SocialMediaPlatforms.isPlatformExists(socialMedia)) {
                        throw new ValidationException(
                                "social media",
                                socialMedia,
                                "Social media platform not valid"
                        );
                    }
                }
        );
        // validate status
        if(!StoreStatus.isStatusExist(createStoreRequest.getStatus())) {
            throw new ValidationException(
                    "status",
                    createStoreRequest.getStatus(),
                    "Status not valid"
            );
        }
        // create a new store
        Store store = storeMapper.createStoreRequestToStore(createStoreRequest);
        store.setCompanyId(companyId);
        store.setCreationDate(LocalDate.now());
        storeRepository.save(store);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Store created successfully")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.CREATED.value())
                .data(
                        storeMapper.storeToStoreDto(store, StoreResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse getStoreDetails(String storeId) {
        // get store
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Store",
                        "id",
                        storeId,
                        "Store does not exist"
                )
        );
        // check store belong to company
        if (store.getCompanyId() != null) {
            if (!companyService.existsById(store.getCompanyId())) {
                throw new ValidationException(
                        "company",
                        store.getCompanyId(),
                        "Company does not exist"
                );
            }
        } else {
            throw new ValidationException(
                    "company",
                    null,
                    "Company does not exist"
            );
        }
        // get store workers
        List<Object> workers = companyService.getStoreWorkers(store.getCompanyId(), storeId);
        // get store managers
        List<Object> managers = companyService.getStoreManagers(store.getCompanyId(), storeId);
        // get store products
        // TODO: get store products

        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Store details retrieved successfully")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .data(
                        Map.of(
                                "store", storeMapper.storeToStoreDto(store, StoreResponse.class),
                                "workers", workers,
                                "managers", managers
                        )
                )
                .build();
    }

    @Override
    public ApiResponse updateStore(String storeId, UpdateStoreRequest updateStoreRequest) {
        // get store
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Store",
                        storeId,
                        "Store does not exist"
                )
        );
        // check store belong to company
        if (store.getCompanyId() != null) {
            if (!companyService.existsById(store.getCompanyId())) {
                throw new ValidationException(
                        "company",
                        store.getCompanyId(),
                        "Company does not exist"
                );
            }
        } else {
            throw new ValidationException(
                    "company",
                    null,
                    "Company does not exist"
            );
        }
        // validate email address
        if (updateStoreRequest.getEmail() != null) {
            if (!storeRepository.existsByIdAndCompanyId(updateStoreRequest.getEmail(), store.getCompanyId())) {
                throw new DuplicateResourceException(
                        "Store",
                        "email",
                        updateStoreRequest.getEmail(),
                        "Email already exists"
                );
            }
        }
        // validate phone number
        if (updateStoreRequest.getPhoneNumber() != null) {
            if (!storeRepository.existsByIdAndCompanyId(updateStoreRequest.getPhoneNumber(), store.getCompanyId())) {
                throw  new DuplicateResourceException(
                        "Store",
                        "phone number",
                        updateStoreRequest.getPhoneNumber(),
                        "Phone number already exists"
                );
            }
        }
        // validate social media links
        updateStoreRequest.getSocialMedia().keySet().forEach(
                socialMedia -> {
                    if(!SocialMediaPlatforms.isPlatformExists(socialMedia)) {
                        throw new ValidationException(
                                "social media",
                                socialMedia,
                                "Social media platform not valid"
                        );
                    }
                }
        );
        // validate status
        if(!StoreStatus.isStatusExist(updateStoreRequest.getStatus())) {
            throw new ValidationException(
                    "status",
                    updateStoreRequest.getStatus(),
                    "Status not valid"
            );
        }
        // update store
        store = storeMapper.updateStoreRequestToStore(updateStoreRequest, store);
        storeRepository.save(store);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Store updated successfully")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .data(
                        storeMapper.storeToStoreDto(store, StoreResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse getAllWorkersOfStore(String storeId) {
        // get store
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Store",
                        storeId,
                        "Store does not exist"
                )
        );
        // check store belong to company
        if (store.getCompanyId() != null) {
            if (!companyService.existsById(store.getCompanyId())) {
                throw new ValidationException(
                        "company",
                        store.getCompanyId(),
                        "Company does not exist"
                );
            }
        } else {
            throw new ValidationException(
                    "company",
                    null,
                    "Company does not exist"
            );
        }
        // get store workers
        List<Object> workers = companyService.getStoreWorkers(store.getCompanyId(), storeId);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Store workers retrieved successfully")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .data(workers)
                .build();
    }

    @Override
    public ApiResponse getAllManagersOfStore(String storeId) {
        // get store
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Store",
                        storeId,
                        "Store does not exist"
                )
        );
        // check store belong to company
        if (store.getCompanyId() != null) {
            if (!companyService.existsById(store.getCompanyId())) {
                throw new ValidationException(
                        "company",
                        store.getCompanyId(),
                        "Company does not exist"
                );
            }
        } else {
            throw new ValidationException(
                    "company",
                    null,
                    "Company does not exist"
            );
        }
        // get store managers
        List<Object> managers = companyService.getStoreManagers(store.getCompanyId(), storeId);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Store managers retrieved successfully")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .data(managers)
                .build();
    }

    @Override
    public ApiResponse setStoreManager(String storeId, String managerId) {
        // get store
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Store",
                        storeId,
                        "Store does not exist"
                )
        );
        // check store belong to company
        if (store.getCompanyId() != null) {
            if (!companyService.existsById(store.getCompanyId())) {
                throw new ValidationException(
                        "company",
                        store.getCompanyId(),
                        "Company does not exist"
                );
            }
        } else {
            throw new ValidationException(
                    "company",
                    null,
                    "Company does not exist"
            );
        }
        // check manager
        if (companyService.isWorkerBelongToStoreAndManager(store.getId(), managerId)) {
            // set store manager
            store.setManagerId(managerId);
            storeRepository.save(store);
            return ApiResponse.builder()
                    .status(HttpStatus.OK.getReasonPhrase())
                    .message("Store manager set successfully")
                    .timestamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value())
                    .data(
                            storeMapper.storeToStoreDto(store, StoreResponse.class)
                    )
                    .build();
        } else {
            throw new ValidationException(
                    "manager",
                    managerId,
                    "Manager does not exist"
            );
        }
    }

    @Override
    public ApiResponse setStoreStatus(String storeId, String storeStatus) {
        // get store
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Store",
                        storeId,
                        "Store does not exist"
                )
        );
        // validate status
        if(!StoreStatus.isStatusExist(storeStatus)) {
            throw new ValidationException(
                    "status",
                    storeStatus,
                    "Status not valid"
            );
        }
        // set store status
        store.setStatus(StoreStatus.valueOf(storeStatus));
        storeRepository.save(store);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Store status set successfully")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .data(
                        storeMapper.storeToStoreDto(store, StoreResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse getStoreProfile(String storeId) {
        // get store
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Store",
                        storeId,
                        "Store does not exist"
                )
        );
        // check store belong to company
        if (store.getCompanyId() != null) {
            if (!companyService.existsById(store.getCompanyId())) {
                throw new ValidationException(
                        "company",
                        store.getCompanyId(),
                        "Company does not exist"
                );
            }
        } else {
            throw new ValidationException(
                    "company",
                    null,
                    "Company does not exist"
            );
        }
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Store profile retrieved successfully")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .data(
                        storeMapper.storeToStoreDto(store, StoreResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse updateStoreAddress(String storeId, String addressId) {
        // get store
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Store",
                        storeId,
                        "Store does not exist"
                )
        );
        // check store belong to company
        if (store.getCompanyId() != null) {
            if (!companyService.existsById(store.getCompanyId())) {
                throw new ValidationException(
                        "company",
                        store.getCompanyId(),
                        "Company does not exist"
                );
            }
        } else {
            throw new ValidationException(
                    "company",
                    null,
                    "Company does not exist"
            );
        }
        // update store address
        store.setAddressId(addressId);
        storeRepository.save(store);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Store address updated successfully")
                .timestamp(LocalDateTime.now())
                .statusCode(HttpStatus.OK.value())
                .data(
                        storeMapper.storeToStoreDto(store, StoreResponse.class)
                )
                .build();
    }
}
