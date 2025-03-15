package com.ataya.company.dto.worker.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegisterWorkerRequest {
    @NotNull(message = "username is required")
    @NotEmpty(message = "can not be empty")
    private String username;

    @NotNull(message = "email is required")
    @NotEmpty(message = "can not be empty")
    private String email;

    @NotNull(message = "password is required")
    @NotEmpty(message = "can not be empty")
    private String password;

    @JsonProperty("store-id")
    private String StoreId;
}
