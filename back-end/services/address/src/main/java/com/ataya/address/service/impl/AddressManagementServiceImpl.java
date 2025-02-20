package com.ataya.address.service.impl;

import com.ataya.address.dto.address.request.CreateAddressByCoordinatesRequest;
import com.ataya.address.dto.address.request.CreateAddressRequest;
import com.ataya.address.enums.AddressTag;
import com.ataya.address.exception.Custom.ValidationException;
import com.ataya.address.model.Address;
import com.ataya.address.repository.AddressRepository;
import com.ataya.address.service.AddressManagementService;
import com.ataya.address.service.HereGeoCoderService;
import com.ataya.address.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;

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
            throw new RestClientException("Address not found");
        }
        address.setAddressDetails(request.getAddressDetails());
        address.setAddressTags(request.getAddressTags().stream().map(AddressTag::valueOf).toList());
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
        address.setAddressTags(request.getAddressTags().stream().map(AddressTag::valueOf).toList());
        addressRepository.save(address);
        return ApiResponse.builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .message("Address created successfully")
                .timestamp(LocalDateTime.now())
                .build();
    }


}
