package com.ataya.beneficiary.dto.beneficiary;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CredentialRequest {
    private String identityNumber;
    private String password;
}
