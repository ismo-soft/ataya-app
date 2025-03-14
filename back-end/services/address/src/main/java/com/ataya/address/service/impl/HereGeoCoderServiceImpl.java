/*
* TODO: this service needs to map the response from the Here API to the Address model
* */
package com.ataya.address.service.impl;

import com.ataya.address.dto.address.request.CreateAddressByCoordinatesRequest;
import com.ataya.address.dto.address.request.UpdateAddressRequest;
import com.ataya.address.dto.here.geocoder.AddressItemDTO;
import com.ataya.address.dto.here.geocoder.HereGeocoderResponse;
import com.ataya.address.dto.address.request.CreateAddressRequest;
import com.ataya.address.model.Address;
import com.ataya.address.service.HereGeoCoderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HereGeoCoderServiceImpl implements HereGeoCoderService {

    private final RestTemplate restTemplate;

    @Value("${ataya.app.here.app.id}")
    private String HERE_APP_ID;

    @Value("${ataya.app.here.api.key}")
    private String HERE_API_KEY;

    @Value("${ataya.app.here.success.score}")
    private Double SUCCESS_SCORE;

    public HereGeoCoderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public Address getAddress(CreateAddressRequest request) {
        final String freeTextAddress =
                request.getDistrict() + ", " +
                request.getStreet() + ", " +
                request.getHouseNumber() + ", " +
                request.getCounty() + "/" +
                request.getState();
        final String url = "https://geocode.search.hereapi.com/v1/geocode?" +
                "q=" + freeTextAddress +
                "&apiKey=" + HERE_API_KEY;
        HereGeocoderResponse response = restTemplate.getForObject(url, HereGeocoderResponse.class);
        if (response == null || response.getItems().isEmpty()) {
            return null;
        }
        if (response.getItems().get(0).getScoring().getQueryScore() < SUCCESS_SCORE) {
            return null;
        }
        return getAddress(response);

    }

    @Override
    public Address getAddress(CreateAddressByCoordinatesRequest request) {
        final String freeTextPosition = request.getLat() + "," + request.getLng();
        final String url = "https://revgeocode.search.hereapi.com/v1/revgeocode?" +
                "at=" + freeTextPosition +
                "&lang=en-US" +
                "&apiKey=" + HERE_API_KEY;
        HereGeocoderResponse response = restTemplate.getForObject(url, HereGeocoderResponse.class);
        if (response == null || response.getItems().isEmpty()) {
            return null;
        }
        return getAddress(response);
    }

    private Address getAddress(HereGeocoderResponse response) {
        AddressItemDTO addressItemDTO = response.getItems().get(0);
        return Address.builder()
                .addressId(addressItemDTO.getId())
                .label(addressItemDTO.getAddress().getLabel())
                .countryCode(addressItemDTO.getAddress().getCountryCode())
                .countryName(addressItemDTO.getAddress().getCountryName())
                .stateCode(addressItemDTO.getAddress().getStateCode())
                .state(addressItemDTO.getAddress().getState())
                .county(addressItemDTO.getAddress().getCounty())
                .city(addressItemDTO.getAddress().getCity())
                .district(addressItemDTO.getAddress().getDistrict())
                .street(addressItemDTO.getAddress().getStreet())
                .postalCode(addressItemDTO.getAddress().getPostalCode())
                .houseNumber(addressItemDTO.getAddress().getHouseNumber())
                .lat(addressItemDTO.getPosition().getLat())
                .lng(addressItemDTO.getPosition().getLng())
                .build();
    }

    @Override
    public Address getAddress(UpdateAddressRequest request) {
        final String freeTextAddress =
                request.getDistrict() + ", " +
                        request.getStreet() + ", " +
                        request.getHouseNumber() + ", " +
                        request.getCounty() + "/" +
                        request.getState();
        final String url = "https://geocode.search.hereapi.com/v1/geocode?" +
                "q=" + freeTextAddress +
                "&apiKey=" + HERE_API_KEY;
        HereGeocoderResponse response = restTemplate.getForObject(url, HereGeocoderResponse.class);
        if (response == null || response.getItems().isEmpty()) {
            return null;
        }
        if (response.getItems().get(0).getScoring().getQueryScore() < SUCCESS_SCORE) {
            return null;
        }
        return getAddress(response);

    }
}
