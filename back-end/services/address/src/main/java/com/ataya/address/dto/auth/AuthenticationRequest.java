package com.ataya.address.dto.auth;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    private String username;
    private String password;
}
