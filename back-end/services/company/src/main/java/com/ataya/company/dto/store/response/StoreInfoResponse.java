package com.ataya.company.dto.store.response;

import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.enums.StoreStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreInfoResponse {
    private String id;
    private String name;
    private String storeCode;
    private String description;
    private String profilePhoto;
    private String email;
    private String phoneNumber;
    private String website;
    private Map<SocialMediaPlatforms, String> socialMedia;
    private LocalDate creationDate;
    private StoreStatus status;
    private String addressId;
    private String companyId;
    private String bankAccountId;
    private String managerId;

}
