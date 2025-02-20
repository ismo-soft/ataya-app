package com.ataya.company.model;

import com.ataya.company.enums.SocialMediaPlatforms;
import com.ataya.company.enums.StoreStatus;
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
@Document(collection = "store")
public class Store {
    @Id
    private String id;
    private String name;
    private String storeCode;
    private String description;
    private String profilePhoto;
    @Indexed(unique = true)
    private String email;
    @Indexed(unique = true)
    private String phone;
    private String website;
    private Map<SocialMediaPlatforms, String> socialMedia;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate creationDate;
    private StoreStatus status;
    private String addressId;
    private String companyId;
    private String bankAccountId;
    private String managerId;

}
