package com.ataya.contributor.dto.user;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContributorDto {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String profilePhoto;
    private String bio;
    private LocalDate registrationDate;
    private String totalContributions;
    private Boolean enabled;

}
