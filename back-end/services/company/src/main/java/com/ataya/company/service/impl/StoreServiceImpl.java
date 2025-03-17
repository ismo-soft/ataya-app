package com.ataya.company.service.impl;

import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreDetailsResponse;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.enums.StoreStatus;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.exception.Custom.ValidationException;
import com.ataya.company.mapper.StoreMapper;
import com.ataya.company.model.Store;
import com.ataya.company.repo.StoreRepository;
import com.ataya.company.service.StoreService;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ataya.company.service.impl.CommonService.addCriteria;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final WorkerService workerService;

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper, WorkerService workerService) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
        this.workerService = workerService;
    }

    @Override
    public Store getStoreById(String storeId) {
        return storeRepository.findById(storeId).orElse(null);
    }

    @Override
    public ApiResponse<StoreInfoResponse> createStore(CreateStoreRequest createStoreRequest, String companyId) {
        // validate social media if not null
        Map<SocialMediaPlatforms, String> socialMedia = stringToSocialMedia(createStoreRequest.getSocialMedia());


        // check if company id is not null
        if (companyId == null || companyId.isEmpty()) {
            throw new ValidationException(
                    "Company Id",
                    null,
                    "User does not belong to any company"
            );
        }
        // create store
        Store store = Store.builder()
                .name(createStoreRequest.getName())
                .storeCode(createStoreRequest.getStoreCode())
                .description(createStoreRequest.getDescription())
                .profilePicture(createStoreRequest.getProfilePicture())
                .email(createStoreRequest.getEmail())
                .phoneNumber(createStoreRequest.getPhoneNumber())
                .website(createStoreRequest.getWebsite())
                .socialMedia(socialMedia)
                .status(createStoreRequest.getStatus())
                .addressId(createStoreRequest.getAddressId())
                .companyId(companyId)
                .creationDate(LocalDate.now())
                .build();
        // save store
        storeRepository.save(store);
        // return store info response
        return ApiResponse.<StoreInfoResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .message("Store created successfully")
                .data(storeMapper.toStoreInfoResponse(store))
                .build();
    }

    private Map<SocialMediaPlatforms, String> stringToSocialMedia(Map<String, String> socialMedia) {
        if (socialMedia == null) {
            return new HashMap<>();
        }
        Map<SocialMediaPlatforms, String> socialMediaMap = new HashMap<>();
        for (Map.Entry<String, String> entry : socialMedia.entrySet()) {
            if (SocialMediaPlatforms.isPlatformExists(entry.getKey())) {
                socialMediaMap.put(SocialMediaPlatforms.getPlatform(entry.getKey()), entry.getValue());
            } else {
                throw new ValidationException("Social Media", entry.getKey(), "Invalid social media platform");
            }
        }
        return socialMediaMap;
    }

    @Override
    public ApiResponse<StoreInfoResponse> getStoreInfo(String storeId) {
        Store store = this.getStoreById(storeId);
        if (store == null) {
            throw new ResourceNotFoundException(
                    "Store",
                    "id",
                    storeId,
                    "Store not found"
            );
        }
        return ApiResponse.<StoreInfoResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Store retrieved successfully")
                .data(storeMapper.toStoreInfoResponse(store))
                .build();
    }

    @Override
    public ApiResponse<List<StoreInfoResponse>> getStores(String name, String storeCode, String description, String status, int page, int size, String companyId) {
        List<Criteria> criteriaList = new ArrayList<>();
        addCriteria(criteriaList, "name", name);
        addCriteria(criteriaList, "storeCode", storeCode);
        addCriteria(criteriaList, "description", description);
        addStatusCriteria(criteriaList, status);
        addCriteria(criteriaList, "companyId", companyId);

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long totalCount = storeRepository.countStoreByQuery(query);
        PageRequest pageRequest = PageRequest.of(page, size);
        query.with(pageRequest);
        List<Store> stores = storeRepository.findStoresByQuery(query);

        return ApiResponse.<List<StoreInfoResponse>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Stores retrieved successfully")
                .total(totalCount)
                .page(page)
                .size(size)
                .totalPages((int) Math.ceil((double) totalCount / size))
                .data(storeMapper.toStoreInfoResponseList(stores))
                .build();
    }

    private void addStatusCriteria(List<Criteria> criteriaList, String status) {
        if (status != null && !status.isEmpty()) {
            List<StoreStatus> statuses = new ArrayList<>();
            for (String s : status.split(",")) {
                if (StoreStatus.isStatusExist(s)) {
                    statuses.add(StoreStatus.getStatus(s));
                } else {
                    throw new ValidationException("Status", s, "Invalid status");
                }
            }
            List<Criteria> statusCriteriaList = statuses.stream()
                    .map(statusObject -> Criteria.where("status").is(statusObject))
                    .toList();
            criteriaList.add(new Criteria().orOperator(statusCriteriaList.toArray(new Criteria[0])));
        }
    }

    @Override
    public ApiResponse<StoreInfoResponse> updateStore(String storeId, UpdateStoreRequest updateStoreRequest) {
        // validate Social Media if not null
        Map<SocialMediaPlatforms, String> socialMedia = stringToSocialMedia(updateStoreRequest.getSocialMedia());


        Store store = this.getStoreById(storeId);
        if (store == null) {
            throw new ResourceNotFoundException(
                    "Store",
                    "id",
                    storeId,
                    "Store not found"
            );
        }
        // update store
        store.setName(updateStoreRequest.getName());
        store.setStoreCode(updateStoreRequest.getStoreCode());
        store.setDescription(updateStoreRequest.getDescription());
        store.setProfilePicture(updateStoreRequest.getProfilePicture());
        store.setEmail(updateStoreRequest.getEmail());
        store.setPhoneNumber(updateStoreRequest.getPhoneNumber());
        store.setWebsite(updateStoreRequest.getWebsite());
        store.setSocialMedia(socialMedia);
        store.setStatus(updateStoreRequest.getStatus());
        store.setAddressId(updateStoreRequest.getAddressId());
        // save store
        storeRepository.save(store);
        // return store info response
        return ApiResponse.<StoreInfoResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Store updated successfully")
                .data(storeMapper.toStoreInfoResponse(store))
                .build();
    }

    @Override
    public boolean isStoreOfCompany(String storeId, String companyId) {
        return storeRepository.existsByIdAndCompanyId(storeId, companyId);
    }

    @Override
    public ApiResponse<StoreDetailsResponse> getStoreWorkers(String storeId, String name, String surname, String username, String email, String phone, int page, int size) {
        StoreDetailsResponse storeDetailsResponse = new StoreDetailsResponse();
        storeDetailsResponse.setStore(getStoreInfo(storeId).getData());
        storeDetailsResponse.setWorkers(workerService.getWorkers(name,surname,username,email,phone,null,storeId,false,page,size).getData());
        return ApiResponse.<StoreDetailsResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Store workers retrieved successfully")
                .data(storeDetailsResponse)
                .build();
    }
}
