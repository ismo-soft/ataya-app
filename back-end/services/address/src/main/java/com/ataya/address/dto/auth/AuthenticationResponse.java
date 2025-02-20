package com.ataya.address.dto.auth;


import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {
    private final String jwt;
}
