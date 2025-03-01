package com.ataya.company.mapper;

import com.ataya.company.dto.company.CompanyDetailsResponse;
import com.ataya.company.model.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CompanyMapper {

    public Object companyToCompanyDto(Company company, Class<?> companyDtoClass) {
        try {
            Object dto = companyDtoClass.getDeclaredConstructor().newInstance();
            Map<String, Field> companyFields = new HashMap<>();

            // Cache company fields for better performance
            Arrays.stream(company.getClass().getDeclaredFields())
                    .forEach(field -> companyFields.put(field.getName(), field));

            Arrays.stream(companyDtoClass.getDeclaredFields())
                    .forEach(dtoField -> {
                        try {
                            dtoField.setAccessible(true);
                            Field companyField = companyFields.get(dtoField.getName());

                            if (companyField != null) {
                                companyField.setAccessible(true);
                                dtoField.set(dto, companyField.get(company));
                            } else {
                                log.debug("Field {} not found in Company entity", dtoField.getName());
                            }
                        } catch (IllegalAccessException e) {
                            log.error("Error accessing field {}: {}", dtoField.getName(), e.getMessage());
                        }
                    });
            return dto;
        } catch (Exception e) {
            log.error("Error mapping Company to DTO: {}", e.getMessage());
            return null;
        }
    }

    public Company CompanyDtoToCompany(Company entity, Object dto, Class<?> dtoClass) {
        try {
            Map<String, Field> entityFields = new HashMap<>();

            // Cache entity fields for better performance
            Arrays.stream(entity.getClass().getDeclaredFields())
                    .forEach(field -> entityFields.put(field.getName(), field));

            Arrays.stream(dtoClass.getDeclaredFields())
                    .forEach(dtoField -> {
                        try {
                            dtoField.setAccessible(true);
                            Field entityField = entityFields.get(dtoField.getName());

                            if (entityField != null) {
                                entityField.setAccessible(true);
                                Object value = dtoField.get(dto);
                                if (value != null) {  // Only set non-null values
                                    entityField.set(entity, value);
                                }
                            } else {
                                log.debug("Field {} not found in Company entity", dtoField.getName());
                            }
                        } catch (IllegalAccessException e) {
                            log.error("Error accessing field {}: {}", dtoField.getName(), e.getMessage());
                        }
                    });

            return entity;
        } catch (Exception e) {
            log.error("Error mapping DTO to Company: {}", e.getMessage());
            return null;
        }
    }

    // Utility method to check if fields match between entity and DTO
    public void validateFieldMapping(Class<?> entityClass, Class<?> dtoClass) {
        Map<String, Field> entityFields = new HashMap<>();
        Arrays.stream(entityClass.getDeclaredFields())
                .forEach(field -> entityFields.put(field.getName(), field));

        Arrays.stream(dtoClass.getDeclaredFields())
                .forEach(dtoField -> {
                    if (!entityFields.containsKey(dtoField.getName())) {
                        log.warn("Field {} exists in DTO but not in entity", dtoField.getName());
                    }
                });
    }


}