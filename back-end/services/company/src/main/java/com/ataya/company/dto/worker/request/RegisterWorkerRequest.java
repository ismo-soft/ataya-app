package com.ataya.company.dto.worker.request;

import lombok.Getter;

@Getter
public class RegisterWorkerRequest {
    private String username;
    private String email;
    private String password;
    private String StoreId;
}
