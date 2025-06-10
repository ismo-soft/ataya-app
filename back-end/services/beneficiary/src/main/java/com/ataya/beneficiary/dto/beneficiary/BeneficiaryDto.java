package com.ataya.beneficiary.dto.beneficiary;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeneficiaryDto {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
}
