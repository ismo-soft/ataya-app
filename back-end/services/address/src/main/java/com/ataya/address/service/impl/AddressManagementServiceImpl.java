/*
* TODO : this service needs to map the response from the Here API to the Address model
* */
package com.ataya.address.service.impl;

import com.ataya.address.dto.address.request.CreateAddressByCoordinatesRequest;
import com.ataya.address.dto.address.request.CreateAddressRequest;
import com.ataya.address.dto.address.request.UpdateAddressRequest;
import com.ataya.address.enums.AddressTag;
import com.ataya.address.exception.Custom.ResourceNotFoundException;
import com.ataya.address.exception.Custom.ValidationException;
import com.ataya.address.model.Address;
import com.ataya.address.repository.AddressRepository;
import com.ataya.address.service.AddressManagementService;
import com.ataya.address.service.HereGeoCoderService;
import com.ataya.address.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AddressManagementServiceImpl implements AddressManagementService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private HereGeoCoderService hereGeoCoderService;

    @Override
    public ApiResponse createAddress(CreateAddressRequest request) {

        request.getAddressTags().forEach(
                tag -> {
                    if (!AddressTag.isValid(tag)) {
                        try {
                            throw new ValidationException(
                                    CreateAddressRequest.class.getDeclaredField("addressTags").getName(),
                                    tag,
                                    "Invalid address tag: " + tag
                            );
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        Address address = hereGeoCoderService.getAddress(request);
        if (address == null) {
            throw new ValidationException(
                    "address",
                    request.getDistrict() + ", " +
                            request.getStreet() + ", " +
                            request.getHouseNumber() + ", " +
                            request.getCounty() + "/" +
                            request.getState(),
                    "Address not found"
            );
        }
        address.setAddressDetails(request.getAddressDetails());
        List<AddressTag> tags = new ArrayList<>();
        request.getAddressTags().forEach(
                tag -> {
                    if (AddressTag.isValid(tag)) {
                        tags.add(AddressTag.valueOf(tag.toUpperCase()));
                    }
                }
        );
        address.setAddressTags(tags);
        address.setLocation(new GeoJsonPoint(address.getLng(), address.getLat()));
        addressRepository.save(address);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .message("Address created successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse createAddress(CreateAddressByCoordinatesRequest request) {
        request.getAddressTags().forEach(
                tag -> {
                    if (!AddressTag.isValid(tag)) {
                        try {
                            throw new ValidationException(
                                    CreateAddressRequest.class.getDeclaredField("addressTags").getName(),
                                    tag,
                                    "Invalid address tag: " + tag
                            );
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        Address address = hereGeoCoderService.getAddress(request);
        if (address == null) {
            throw new RestClientException("Address not found");
        }
        address.setAddressDetails(request.getAddressDetails());
        List<AddressTag> tags = new ArrayList<>();
        request.getAddressTags().forEach(
                tag -> {
                    if (AddressTag.isValid(tag)) {
                        tags.add(AddressTag.valueOf(tag.toUpperCase()));
                    }
                }
        );
        address.setAddressTags(tags);
        address.setLocation(new GeoJsonPoint(address.getLng(), address.getLat()));
        addressRepository.save(address);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .message("Address created successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse updateAddress(UpdateAddressRequest request, String id) {
        request.getAddressTags().forEach(
                tag -> {
                    if (!AddressTag.isValid(tag)) {
                        try {
                            throw new ValidationException(
                                    CreateAddressRequest.class.getDeclaredField("addressTags").getName(),
                                    tag,
                                    "Invalid address tag: " + tag
                            );
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        System.out.println("id = " + id);
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        Address.class.getSimpleName(),
                        id,
                        "Address not found"
                )
        );
        System.out.println("address = " + address);
        Address hereAddress = hereGeoCoderService.getAddress(request);
        address.setAddressDetails(request.getAddressDetails());
        List<AddressTag> tags = new ArrayList<>();
        request.getAddressTags().forEach(
                tag -> {
                    if (AddressTag.isValid(tag)) {
                        tags.add(AddressTag.valueOf(tag.toUpperCase()));
                    }
                }
        );
        System.out.println("tags = " + tags);
        address.setAddressTags(tags);
        address.setAddressId(hereAddress.getAddressId());
        address.setLabel(hereAddress.getLabel());
        address.setCountryCode(hereAddress.getCountryCode());
        address.setCountryName(hereAddress.getCountryName());
        address.setStateCode(hereAddress.getStateCode());
        address.setState(hereAddress.getState());
        address.setCounty(hereAddress.getCounty());
        address.setCity(hereAddress.getCity());
        address.setDistrict(hereAddress.getDistrict());
        address.setStreet(hereAddress.getStreet());
        address.setPostalCode(hereAddress.getPostalCode());
        address.setHouseNumber(hereAddress.getHouseNumber());
        address.setLat(hereAddress.getLat());
        address.setLng(hereAddress.getLng());
        address.setLocation(new GeoJsonPoint(hereAddress.getLng(), hereAddress.getLat()));
        addressRepository.save(address);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Address updated successfully")
                .timestamp(LocalDateTime.now())
                .build();

    }

    @Override
    public ApiResponse getAddress(String id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        Address.class.getSimpleName(),
                        id,
                        "Address not found"
                )
        );
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Address retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(address)
                .build();
    }
}
