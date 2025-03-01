package com.ataya.company.model;

import com.ataya.company.enums.SocialMediaPlatforms;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "company")
public class Company {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String phoneNumber;
    @Indexed(unique = true)
    private String email;
    private String address;
    private String registrationNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataOfIncorporation;
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
