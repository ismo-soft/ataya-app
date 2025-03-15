package com.ataya.company.dto.worker.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    @NotNull(message = "id is required")
    @NotEmpty(message = "can not be empty")
    private String id;

    @NotNull(message = "oldPassword is required")
    @NotEmpty(message = "can not be empty")
    private String oldPassword;

    @NotNull(message = "newPassword is required")
    @NotEmpty(message = "can not be empty")
    private String newPassword;

    @NotNull(message = "confirmPassword is required")
    @NotEmpty(message = "can not be empty")
    private String confirmPassword;

}