package com.ataya.company.dto.worker.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    @NotNull(message = "id is required")
    @NotEmpty(message = "can not be empty")
    @JsonProperty("new-password")
    private String newPassword;

    @NotNull(message = "confirmPassword is required")
    @NotEmpty(message = "can not be empty")
    @JsonProperty("confirm-password")
    private String confirmPassword;
}
