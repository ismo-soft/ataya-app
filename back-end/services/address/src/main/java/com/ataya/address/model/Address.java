package com.ataya.address.model;

import com.ataya.address.enums.AddressTag;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "address")
public class Address {
    @Id
    private String id;
    /*
    * this is the address id from the geocoder service
    * */
    private String addressId;
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
    private List<AddressTag> addressTags;
    private String addressDetails;
    private Double lat;
    private Double lng;
    private GeoJsonPoint location;
}
