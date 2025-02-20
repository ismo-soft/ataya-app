package com.ataya.company.exception.Custom;

import com.ataya.company.enums.ErrorCode;
import com.ataya.company.util.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> additionalDetails;


    public BaseException(ErrorCode errorCode, String errorDescription,Map<String, Object> additionalDetails) {
        super(errorDescription);
        this.errorCode = errorCode != null ? errorCode : ErrorCode.UNKNOWN_ERROR;
        this.additionalDetails = additionalDetails != null ? additionalDetails : Map.of();
    }
}
