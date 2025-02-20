package com.ataya.company.dto.worker.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}