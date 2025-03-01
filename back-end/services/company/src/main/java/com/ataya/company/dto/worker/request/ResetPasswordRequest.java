package com.ataya.company.dto.worker.request;

import lombok.Getter;

@Getter
public class ResetPasswordRequest {
    private String newPassword;
    private String confirmPassword;
}
