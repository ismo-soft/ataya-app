package com.ataya.company.dto.store.request;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStoreRequest {
    private String name;
    private String storeCode;
    private String description;
    private String profilePicture;
    private String email;
    private String phoneNumber;
    private String website;
    private Map<String, String> socialMedia;
    private String status;
}
