package com.ataya.company.dto.worker.request;

import lombok.Getter;

@Getter
public class ChangeUsernameRequest {
    private String id;
    private String newUsername;
    private String password;
}
