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
    private String phoneNumber;
    private String registrationNumber;
    private String address;
    private LocalDate dateOfIncorporation;
    private String website;
    private String description;
    private Map<String, String> socialMedia;
    private String legalEntityType;
    private String sector;
    private String industry;
    private String taxId;
    private String currency;
    private String addressId;
}
