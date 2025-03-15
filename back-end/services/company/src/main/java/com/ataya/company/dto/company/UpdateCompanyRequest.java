package com.ataya.company.dto.company;

import com.ataya.company.enums.SocialMediaPlatforms;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Getter
public class UpdateCompanyRequest {
    private String name;
    private String email;
    @JsonProperty("phone-number")
    private String phoneNumber;
    @JsonProperty("registration-number")
    private String registrationNumber;
    private String address;
    @JsonProperty("date-of-incorporation")
    private LocalDate dateOfIncorporation;
    private String website;
    private String logo;
    private String description;
    @JsonProperty("cover-photo")
    private String coverPhoto;
    @JsonProperty("profile-photo")
    private String profilePhoto;
    @JsonProperty("social-media")
    private Map<SocialMediaPlatforms, String> socialMedia;
    @JsonProperty("legal-entity-type")
    private String legalEntityType;
    private String sector;
    private String industry;
    @JsonProperty("tax-id")
    private String taxId;
    private String currency;
    private String addressId;
}
