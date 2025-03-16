package com.ataya.company.service.impl;

import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.enums.StoreStatus;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.exception.Custom.ValidationException;
import com.ataya.company.mapper.StoreMapper;
import com.ataya.company.model.Store;
import com.ataya.company.repo.StoreRepository;
import com.ataya.company.service.StoreService;
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

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    public StoreServiceImpl(StoreRepository storeRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.storeMapper = storeMapper;
    }

    @Override
    public Store getStoreById(String storeId) {
        return storeRepository.findById(storeId).orElse(null);
    }

    @Override
    public ApiResponse<StoreInfoResponse> createStore(CreateStoreRequest createStoreRequest, String companyId) {
        // validate social media if not null
        Map<SocialMediaPlatforms,String> socialMedia = new HashMap<>();
        if (createStoreRequest.getSocialMedia() != null) {
            for (String key : createStoreRequest.getSocialMedia().keySet()) {
                if(!SocialMediaPlatforms.isPlatformExists(key)) {
                    throw new ValidationException(
                            "Social Media",
                            key,
                            "Invalid social media platform"
                    );
                }
                socialMedia.put(SocialMediaPlatforms.getPlatform(key),createStoreRequest.getSocialMedia().get(key));
            }
        }


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
        List<String> names = name == null ? null : List.of(name.split(","));
        List<String> storeCodes = storeCode == null ? null : List.of(storeCode.split(","));
        List<String> descriptions = description == null ? null : List.of(description.split(","));
        // convert status to list of StoreStatus if not null and be sure that all values are valid
        List<StoreStatus> statuses = new ArrayList<>();
        if (status != null) {
            for (String s : status.split(",")) {
                if (StoreStatus.isStatusExist(status)) {
                    statuses.add(StoreStatus.getStatus(s));
                } else {
                    throw new ValidationException(
                            "Status",
                            status,
                            "Invalid status"
                    );
                }
            }
        }
        List<String> companyIds = companyId == null ? null : List.of(companyId.split(","));

        List<Criteria> criteriaList = new ArrayList<>();
        if (names != null && !names.isEmpty()) {
            List<Criteria> nameCriteriaList = names.stream()
                    .map(str -> Criteria.where("name").regex(".*" + str + ".*", "i"))
                    .toList();
            criteriaList.add(new Criteria().orOperator(nameCriteriaList.toArray(new Criteria[0])));
        }
        if (storeCodes != null && !storeCodes.isEmpty()) {
            List<Criteria> storeCodeCriteriaList = storeCodes.stream()
                    .map(str -> Criteria.where("storeCode").regex(".*" + str + ".*", "i"))
                    .toList();
            criteriaList.add(new Criteria().orOperator(storeCodeCriteriaList.toArray(new Criteria[0])));
        }
        if (descriptions != null && !descriptions.isEmpty()) {
            List<Criteria> descriptionCriteriaList = descriptions.stream()
                    .map(str -> Criteria.where("description").regex(".*" + str + ".*", "i"))
                    .toList();
            criteriaList.add(new Criteria().orOperator(descriptionCriteriaList.toArray(new Criteria[0])));
        }
        if (!statuses.isEmpty()) {
            List<Criteria> statusCriteriaList = statuses.stream()
                    .map(statusObject -> Criteria.where("status").is(statusObject))
                    .toList();
            criteriaList.add(new Criteria().orOperator(statusCriteriaList.toArray(new Criteria[0])));
        }
        if (companyIds != null && !companyIds.isEmpty()) {
            List<Criteria> companyIdCriteriaList = companyIds.stream()
                    .map(str -> Criteria.where("companyId").is(str))
                    .toList();
            criteriaList.add(new Criteria().orOperator(companyIdCriteriaList.toArray(new Criteria[0])));
        }


        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long totalCount = storeRepository.countBookByCriteria(query);
        PageRequest pageRequest = PageRequest.of(page, size);
        query.with(pageRequest);
        List<Store> stores = storeRepository.findStoresByCriteria(query);

        // return store info response
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

    @Override
    public ApiResponse<StoreInfoResponse> updateStore(String storeId, UpdateStoreRequest updateStoreRequest) {
        // validate Social Media if not null
        Map<SocialMediaPlatforms,String> socialMedia = new HashMap<>();
        if (updateStoreRequest.getSocialMedia() != null) {
            for (String key : updateStoreRequest.getSocialMedia().keySet()) {
                if(!SocialMediaPlatforms.isPlatformExists(key)) {
                    throw new ValidationException(
                            "Social Media",
                            key,
                            "Invalid social media platform"
                    );
                }
                socialMedia.put(SocialMediaPlatforms.getPlatform(key),updateStoreRequest.getSocialMedia().get(key));
            }
        }

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
}
