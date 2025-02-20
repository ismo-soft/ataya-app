package com.ataya.address.exception.Custom;

import com.ataya.address.enums.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidOperationException extends BaseException {

    private final String operationName;
    private final String reason;

    public InvalidOperationException(String operationName, String reason) {
        super(
                ErrorCode.INVALID_OPERATION,
                reason != null ? reason : "Invalid operation",
                Map.of("operationName", operationName, "reason", reason)
        );
        this.operationName = operationName;
        this.reason = reason;
    }
}
