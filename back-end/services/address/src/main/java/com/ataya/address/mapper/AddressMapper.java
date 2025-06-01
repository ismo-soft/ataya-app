package com.ataya.address.mapper;

import com.ataya.address.dto.address.response.AddressInfoResponse;
import com.ataya.address.model.Address;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Service
public class AddressMapper {

    public AddressInfoResponse toDto(Address address) {
        return AddressInfoResponse.builder()
                .id(address.getId())
                .addressId(address.getAddressId())
                .label(address.getLabel())
                .countryCode(address.getCountryCode())
                .countryName(address.getCountryName())
                .stateCode(address.getStateCode())
                .state(address.getState())
                .county(address.getCounty())
                .city(address.getCity())
                .district(address.getDistrict())
                .street(address.getStreet())
                .houseNumber(address.getHouseNumber())
                .postalCode(address.getPostalCode())
                .addressDetails(address.getAddressDetails())
                .addressTags(address.getAddressTags().stream().map(Enum::name).toList())
                .lat(address.getLat())
                .lng(address.getLng())
                .location(address.getLocation())
                .belongsTo(address.getBelongsTo())
                .build();

    }

}
