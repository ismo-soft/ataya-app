package com.ataya.company.dto.worker.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotNull(message = "username is required")
    @NotEmpty(message = "can not be empty")
    private String username;

    @NotNull(message = "email is required")
    @NotEmpty(message = "can not be empty")
    private String email;

    @NotNull(message = "password is required")
    @NotEmpty(message = "can not be empty")
    private String password;
}
