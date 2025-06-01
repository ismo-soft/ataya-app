package com.ataya.address.dto.address.response;

import com.ataya.address.enums.AddressTag;
import lombok.*;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressInfoResponse {
    private String id;
    private String addressId;
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
    private List<String> addressTags;
    private String addressDetails;
    private Double lat;
    private Double lng;
    private GeoJsonPoint location;
    private String belongsTo;
}
