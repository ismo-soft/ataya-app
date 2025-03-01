package com.ataya.company.mapper;

import com.ataya.company.dto.store.request.CreateStoreRequest;
import com.ataya.company.dto.store.request.UpdateStoreRequest;
import com.ataya.company.dto.store.response.StoreResponse;
import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.enums.StoreStatus;
import com.ataya.company.model.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StoreMapper {
    public Object storeToStoreDto(Store store, Class<?> dtoClass) {
        try {
            Object dto = dtoClass.getDeclaredConstructor().newInstance();
            Map<String, Field> storeFields = new HashMap<>();

            Arrays.stream(store.getClass().getDeclaredFields())
                    .forEach(field -> storeFields.put(field.getName(), field));

            Arrays.stream(dtoClass.getDeclaredFields())
                    .forEach(dtoField -> {
                        try {
                            dtoField.setAccessible(true);
                            Field storeField = storeFields.get(dtoField.getName());

                            if (storeField != null) {
                                storeField.setAccessible(true);
                                dtoField.set(dto, storeField.get(store));
                            } else {
                                log.debug("Field {} not found in Store entity", dtoField.getName());
                            }
                        } catch (IllegalAccessException e) {
                            log.error("Error accessing field {}: {}", dtoField.getName(), e.getMessage());
                        }
                    });
            return dto;
        } catch (Exception e) {
            log.error("Error mapping Store to DTO: {}", e.getMessage());
            return null;
        }
    }

    public Store createStoreRequestToStore(CreateStoreRequest createStoreRequest) {
        Map<SocialMediaPlatforms, String> socialMedia = new HashMap<>();
        createStoreRequest.getSocialMedia().forEach(
                (key, value) -> {
                    if (!SocialMediaPlatforms.isPlatformExists(key)) {
                        socialMedia.put(SocialMediaPlatforms.valueOf(key), value);
                    } else {
                        log.debug("Social media platform {} not found", key);
                    }
                }
        );
        StoreStatus storeStatus = null;
        if (StoreStatus.isStatusExist(createStoreRequest.getStatus())) {
            storeStatus = StoreStatus.valueOf(createStoreRequest.getStatus());
        } else {
            log.debug("Store status {} not found", createStoreRequest.getStatus());
        }

        return Store.builder()
                .name(createStoreRequest.getName())
                .storeCode(createStoreRequest.getStoreCode())
                .description(createStoreRequest.getDescription())
                .profilePicture(createStoreRequest.getProfilePicture())
                .email(createStoreRequest.getEmail())
                .socialMedia(socialMedia)
                .status(storeStatus)
                .phoneNumber(createStoreRequest.getPhoneNumber())
                .website(createStoreRequest.getWebsite())
                .addressId(createStoreRequest.getAddressId())
                .build();
    }

    public Store updateStoreRequestToStore(UpdateStoreRequest updateStoreRequest, Store store) {
        Map<SocialMediaPlatforms, String> socialMedia = new HashMap<>();
        updateStoreRequest.getSocialMedia().forEach(
                (key, value) -> {
                    if (!SocialMediaPlatforms.isPlatformExists(key)) {
                        socialMedia.put(SocialMediaPlatforms.valueOf(key), value);
                    } else {
                        log.debug("Social media platform {} not found", key);
                    }
                }
        );
        StoreStatus storeStatus = null;
        if (StoreStatus.isStatusExist(updateStoreRequest.getStatus())) {
            storeStatus = StoreStatus.valueOf(updateStoreRequest.getStatus());
        } else {
            log.debug("Store status {} not found", updateStoreRequest.getStatus());
        }

        return Store.builder()
                .id(store.getId())
                .name(updateStoreRequest.getName())
                .storeCode(updateStoreRequest.getStoreCode())
                .description(updateStoreRequest.getDescription())
                .profilePicture(updateStoreRequest.getProfilePicture())
                .email(updateStoreRequest.getEmail())
                .socialMedia(socialMedia)
                .status(storeStatus)
                .phoneNumber(updateStoreRequest.getPhoneNumber())
                .website(updateStoreRequest.getWebsite())
                .build();
    }
}
