package com.ataya.company.dto.worker.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangePhoneRequest {
    @NotNull(message = "id is required")
    @NotEmpty(message = "can not be empty")
    private String id;

    @NotNull(message = "phoneNumber is required")
    @NotEmpty(message = "can not be empty")
    @JsonProperty("phone-number")
    private String phoneNumber;

    @NotNull(message = "password is required")
    @NotEmpty(message = "can not be empty")
    private String password;
}
