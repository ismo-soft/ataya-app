package com.ataya.company.dto.worker.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    @NotNull(message = "id is required")
    @NotEmpty(message = "can not be empty")
    private String newPassword;

    @NotNull(message = "confirmPassword is required")
    @NotEmpty(message = "can not be empty")
    private String confirmPassword;
}
