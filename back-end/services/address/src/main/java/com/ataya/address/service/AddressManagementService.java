package com.ataya.address.service;

import com.ataya.address.dto.address.request.CreateAddressByCoordinatesRequest;
import com.ataya.address.dto.address.request.CreateAddressRequest;
import com.ataya.address.dto.address.request.UpdateAddressRequest;
import com.ataya.address.util.ApiResponse;

public interface AddressManagementService {
    
    ApiResponse createAddress(CreateAddressRequest request);
    
    ApiResponse createAddress(CreateAddressByCoordinatesRequest request);

    ApiResponse updateAddress(UpdateAddressRequest request, String id);

    ApiResponse getAddress(String id);
}
