package com.ataya.company.dto.worker.request;

import lombok.Getter;

@Getter
public class ChangeEmailRequest {
    private String id;
    private String email;
    private String password;
}
