package com.ataya.contributor.exception.custom;

import com.ataya.contributor.enums.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends BaseException{

    private final String fieldName;
    private final Object fieldValue;

    public ValidationException(String fieldName, Object fieldValue, String errorDescription) {
        super(
                ErrorCode.NOT_VALID,
                errorDescription != null ? errorDescription : "Validation failed",
                Map.of("fieldName", fieldName, "fieldValue", fieldValue)
        );
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
