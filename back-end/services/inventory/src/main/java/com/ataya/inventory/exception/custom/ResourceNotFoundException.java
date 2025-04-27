package com.ataya.inventory.exception.custom;

import com.ataya.inventory.enums.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class ResourceNotFoundException extends BaseException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;


    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Resource not found",
                Map.of("resourceName", resourceName, "fieldName", fieldName, "fieldValue", fieldValue)
        );
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue, String errorDescription) {
        super(
                ErrorCode.RESOURCE_NOT_FOUND,
                errorDescription,
                Map.of("resourceName", resourceName, "fieldName", fieldName, "fieldValue", fieldValue)
        );
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }



}
