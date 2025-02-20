package com.ataya.company.dto.company;


import com.ataya.company.enums.SocialMediaPlatforms;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDetailsResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String registrationNumber;
    private LocalDate dateOfRegistration;
    private String website;
    private String logo;
    private String description;
    private String coverPhoto;
    private String profilePhoto;
    private Map<SocialMediaPlatforms, String> socialMedia;
    private String legalEntityType;
    private String sector;
    private String industry;
    private String taxId;
    private String currency;
    private LocalDate enrollmentDate;
    private String bankAccountId;
    private String addressId;
    private String ceoId;
}
