package com.ataya.contributor.dto.user;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private ContributorDto contributor;
    private String token;
}
