package com.ataya.beneficiary.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private final String message;
    private final int httpcode;
    private final String httpstatus;
    private final String timestamp;
    private final String path;
    private final String method;
    private final Map<String, Object> additionalDetails;

    // toString method
    @Override
    public String toString() {
        // return in json format
        return "{" +
                "\"message\":\"" + message + '\"' +
                ", \"httpcode\":" + httpcode +
                ", \"httpstatus\":\"" + httpstatus + '\"' +
                ", \"timestamp\":\"" + timestamp + '\"' +
                ", \"path\":\"" + path + '\"' +
                ", \"method\":\"" + method + '\"' +
                ", \"additionalDetails\":" + additionalDetails +
                '}';
    }
}
