package com.ataya.company.dto.store.response;

import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.enums.StoreStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreResponse {
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
