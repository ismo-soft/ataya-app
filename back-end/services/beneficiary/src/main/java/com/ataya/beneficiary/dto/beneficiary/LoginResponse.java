package com.ataya.beneficiary.dto.beneficiary;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private BeneficiaryDto beneficiaryDto;
}
