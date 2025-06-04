package com.ataya.address.service;

import com.ataya.address.dto.address.request.CreateAddressByCoordinatesRequest;
import com.ataya.address.dto.address.request.CreateAddressRequest;
import com.ataya.address.dto.address.request.UpdateAddressRequest;
import com.ataya.address.model.Address;

public interface HereGeoCoderService {
    Address getAddress(CreateAddressRequest request);
    Address getAddress(CreateAddressByCoordinatesRequest request);
    Address getAddress(UpdateAddressRequest request);

    boolean setAddressIdToStore(String belongsTo, String id);
}
