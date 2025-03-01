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
public class UpdateCompanyRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String registrationNumber;
    private LocalDate dateOfIncorporation;
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
    private String addressId;
}
