package com.ataya.company.mapper;

import com.ataya.company.dto.store.response.StoreResponse;
import com.ataya.company.model.Store;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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
}
