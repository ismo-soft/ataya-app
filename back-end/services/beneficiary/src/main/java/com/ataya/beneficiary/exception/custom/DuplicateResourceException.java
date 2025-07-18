package com.ataya.beneficiary.exception.custom;

import com.ataya.contributor.enums.ErrorCode;
import com.ataya.contributor.exception.custom.BaseException;
import lombok.Getter;

import java.util.Map;

@Getter
public class DuplicateResourceException extends BaseException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue, String errorDescription) {
        super(
                ErrorCode.DUPLICATE_RESOURCE,
                errorDescription != null ? errorDescription : "Resource already exists",
                Map.of("resourceName", resourceName, "fieldName", fieldName, "fieldValue", fieldValue)
        );
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
