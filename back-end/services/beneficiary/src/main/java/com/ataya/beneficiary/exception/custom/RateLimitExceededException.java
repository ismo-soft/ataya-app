package com.ataya.beneficiary.exception.custom;


import com.ataya.contributor.enums.ErrorCode;
import com.ataya.contributor.exception.custom.BaseException;
import lombok.Getter;

import java.util.Map;

@Getter
public class RateLimitExceededException extends BaseException {
    public RateLimitExceededException(String errorDescription, Map<String, Object> additionalDetails) {
        super(
                ErrorCode.RATE_LIMIT_EXCEEDED,
                errorDescription,
                additionalDetails
        );
    }
}
