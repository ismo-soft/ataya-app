package com.ataya.company.dto.worker.response;

import lombok.*;

import java.util.List;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WorkerInfoResponse {
    private String id;
    private String username;
    private String email;
    private String phone;
    private String token;
    private String storeId;
    private String companyId;
    private String name;
    private String surname;
    private String profilePicture;
    private List<String> roles;
}
