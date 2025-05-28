package com.ataya.contributor.dto.user;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialRequest {
    private String email;
    private String password;
}
