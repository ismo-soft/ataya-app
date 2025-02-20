package com.ataya.company.dto.worker.request;

import lombok.Getter;

@Getter
public class ChangePhoneRequest {
    private String id;
    private String phone;
    private String password;
}
