package com.ataya.address.dto.address.response;

import com.ataya.address.enums.AddressTag;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressInfoResponse {
    private String id;
    private String label;
    private String countryCode;
    private String countryName;
    private String stateCode;
    private String state;
    private String county;
    private String district;
    private String street;
    private String postalCode;
    private String houseNumber;
    private List<String> AddressTags;
    private String addressDetails;
    private String lat;
    private String lng;
}
