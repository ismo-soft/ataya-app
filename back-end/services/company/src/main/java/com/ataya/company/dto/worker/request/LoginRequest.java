package com.ataya.company.dto.worker.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;

    private String email;

    @NotNull(message = "password is required")
    @NotEmpty(message = "can not be empty")
    private String password;
}
