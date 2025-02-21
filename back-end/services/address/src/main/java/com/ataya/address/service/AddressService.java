package com.ataya.address.service;

import com.ataya.address.util.ApiResponse;

import java.util.List;

public interface AddressService {
    ApiResponse getAddressByCoordinates(Double lat, Double lng);

    ApiResponse getAddressByTags(List<String> tags, Integer page, Integer size);

    ApiResponse getAddressById(String id);

    ApiResponse getAddressByAddressId(String addressId);

    ApiResponse getNearbyAddressesByCoordinates(Double lat, Double lng, Integer distance, List<String> tags, Integer page, Integer size);

    ApiResponse getNearbyAddressesById(String id, Integer distance, List<String> tags, Integer page, Integer size);

    ApiResponse getNearbyAddressesByAddressId(String addressId, Integer distance, List<String> tags, Integer page, Integer size);

    ApiResponse getNearbyAddresses(Double lat, Double lng, String id, String addressId, Integer distance, List<String> tags, Integer page, Integer size);
}
