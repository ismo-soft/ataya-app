package com.ataya.address.dto.address.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAddressRequest {
    private String countryName;
    private String state;
    private String county;
    private String district;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String addressDetails;
    private List<String> addressTags;
    private String belongsTo;
}
