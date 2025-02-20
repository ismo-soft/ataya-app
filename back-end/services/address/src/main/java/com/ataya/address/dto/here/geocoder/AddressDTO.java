package com.ataya.address.dto.here.geocoder;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {
    private String label;
    private String countryCode;
    private String countryName;
    private String stateCode;
    private String state;
    private String county;
    private String city;
    private String district;
    private String street;
    private String postalCode;
    private String houseNumber;
}
