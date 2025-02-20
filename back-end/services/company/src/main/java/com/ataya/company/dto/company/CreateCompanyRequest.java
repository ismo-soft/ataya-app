package com.ataya.company.dto.company;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCompanyRequest {
    private String name;
    private String registrationNumber;
}
