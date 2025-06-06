package com.ataya.address.service.impl;

import com.ataya.address.enums.AddressTag;
import com.ataya.address.exception.Custom.ResourceNotFoundException;
import com.ataya.address.exception.Custom.ValidationException;
import com.ataya.address.mapper.AddressMapper;
import com.ataya.address.model.Address;
import com.ataya.address.repository.AddressRepository;
import com.ataya.address.service.AddressService;
import com.ataya.address.util.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {


    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public ApiResponse getAddressByCoordinates(Double lat, Double lng) {

        Address address = addressRepository.findByLatAndLng(lat, lng).orElseThrow(
                () -> new ResourceNotFoundException(
                        Address.class.getSimpleName(),
                        "[lat, lng]",
                        List.of(lat, lng)
                )
        );
        Object response = addressMapper.toDto(address);
        return ApiResponse.builder()
                .message("Address found")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(response)
                .build();
    }

    @Override
    public ApiResponse getAddressByTags(List<String> tags, Integer page, Integer size) {
        List<AddressTag> addressTags = validateTags(tags);
        if(page !=null && size != null){
            Pageable pageable = PageRequest.of(page, size);
            Page<Address> addresses = addressRepository.findByAddressTagsIn(addressTags, pageable);
            List<Object> response = new ArrayList<>();
            for (Address address : addresses) {
                response.add(addressMapper.toDto(address));
            }
            return ApiResponse.builder()
                    .message("Addresses found")
                    .status(HttpStatus.OK.getReasonPhrase())
                    .statusCode(HttpStatus.OK.value())
                    .timestamp(LocalDateTime.now())
                    .page(page)
                    .size(size)
                    .data(response)
                    .build();
        }else {
            List<Address> addresses = addressRepository.findByAddressTagsIn(addressTags);
            List<Object> response = new ArrayList<>();
            for (Address address : addresses) {
                response.add(addressMapper.toDto(address));
            }
            return ApiResponse.builder()
                    .message("Addresses found")
                    .status(HttpStatus.OK.getReasonPhrase())
                    .statusCode(HttpStatus.OK.value())
                    .timestamp(LocalDateTime.now())
                    .data(response)
                    .build();
        }
    }

    private List<AddressTag> validateTags(List<String> tags) {
        for (String tag : tags) {
            if (!AddressTag.isValid(tag)) {
                throw new ValidationException(
                        AddressTag.class.getSimpleName(),
                        "tag",
                        "Invalid address tag: " + tag
                );
            }
        }
        tags = tags.stream().map(String::toUpperCase).toList();
        return tags.stream().map(AddressTag::valueOf).toList();
    }

    @Override
    public ApiResponse getAddressById(String id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        Address.class.getSimpleName(),
                        "id",
                        id
                )
        );
        Object response = addressMapper.toDto(address);
        return ApiResponse.builder()
                .message("Address found")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(response)
                .build();
    }

    @Override
    public ApiResponse getAddressByAddressId(String addressId) {
        Address address = addressRepository.findByAddressId(addressId).orElseThrow(
                () -> new ResourceNotFoundException(
                        Address.class.getSimpleName(),
                        "addressId",
                        addressId
                )
        );
        Object response = addressMapper.toDto(address);
        return ApiResponse.builder()
                .message("Address found")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(response)
                .build();
    }

    @Override
    public ApiResponse getNearbyAddressesByCoordinates(
            Double lat,
            Double lng,
            Integer distance,
            List<String> tags,
            Integer page,
            Integer size
    ) {
        if (distance <= 0) {
            throw new ValidationException(
                    "distance",
                    distance,
                    "Invalid distance"
            );
        }
        return getApiResponse(lat, lng, distance, tags, page, size);
    }

    private ApiResponse getApiResponse(Double lat, Double lng, Integer distance, List<String> tags, Integer page, Integer size) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Address> addresses;
            if (tags != null && !tags.isEmpty()) {
                addresses = addressRepository.findNearbyAddressesByLocationAndAddressTagsIn(lat,lng, distance, validateTags(tags), pageable);
            } else {
                addresses = addressRepository.findNearbyAddressesByLocation(lat, lng, distance, pageable);
            }
            List<Object> response = new ArrayList<>();
            for (Address address : addresses) {
                response.add(addressMapper.toDto(address));
            }
            return ApiResponse.builder()
                    .message("Nearby addresses found")
                    .status(HttpStatus.OK.getReasonPhrase())
                    .statusCode(HttpStatus.OK.value())
                    .timestamp(LocalDateTime.now())
                    .page(page)
                    .size(size)
                    .data(response)
                    .build();
        } else {
            List<Address> addresses;
            if (tags != null && !tags.isEmpty()) {
                addresses = addressRepository.findNearbyAddressesByLocationAndAddressTagsIn(lat, lng, distance, validateTags(tags), PageRequest.of(0, 10)).toList();
            } else {
                addresses = addressRepository.findNearbyAddressesByLocation(lat, lng, distance, PageRequest.of(0, 10)).toList();
            }
            List<Object> response = new ArrayList<>();
            for (Address address : addresses) {
                response.add(addressMapper.toDto(address));
            }
            return ApiResponse.builder()
                    .message("Nearby addresses found")
                    .status(HttpStatus.OK.getReasonPhrase())
                    .statusCode(HttpStatus.OK.value())
                    .timestamp(LocalDateTime.now())
                    .data(response)
                    .build();
        }
    }

    @Override
    public ApiResponse getNearbyAddressesById(
            String id,
            Integer distance,
            List<String> tags,
            Integer page,
            Integer size
    ) {
        if (distance <= 0) {
            throw new ValidationException(
                    "distance",
                    distance,
                    "Invalid distance"
            );
        }
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        Address.class.getSimpleName(),
                        "id",
                        id
                )
        );
        if (address.getLat() == null || address.getLng() == null) {
            throw new ValidationException(
                    Address.class.getSimpleName(),
                    "[lat, lng]",
                    "Coordinates not found"
            );
        }
        return getApiResponse(
                address.getLat(),
                address.getLng(),
                distance,
                tags,
                page,
                size
        );
    }

    @Override
    public ApiResponse getNearbyAddressesByAddressId(
            String addressId,
            Integer distance,
            List<String> tags,
            Integer page,
            Integer size
    ) {
        Address address = addressRepository.findByAddressId(addressId).orElseThrow(
                () -> new ResourceNotFoundException(
                        Address.class.getSimpleName(),
                        "addressId",
                        addressId
                )
        );
        if (distance <= 0) {
            throw new ValidationException(
                    "distance",
                    distance,
                    "Invalid distance"
            );
        }
        if (address.getLat() == null || address.getLng() == null) {
            throw new ValidationException(
                    Address.class.getSimpleName(),
                    "[lat, lng]",
                    "Coordinates not found"
            );
        }
        return getApiResponse(
                address.getLat(),
                address.getLng(),
                distance,
                tags,
                page,
                size
        );

    }

    @Override
    public ApiResponse getNearbyAddresses(Double lat, Double lng, String id, String addressId, Integer distance, List<String> tags, Integer page, Integer size) {
        if (id != null) {
            return getNearbyAddressesById(id, distance, tags, page, size);
        } else if (addressId != null) {
            return getNearbyAddressesByAddressId(addressId, distance, tags, page, size);
        } else if (lat != null && lng != null) {
            return getNearbyAddressesByCoordinates(lat, lng, distance, tags, page, size);
        } else {
            throw new ValidationException(
                    "lat, lng, id, addressId",
                    "null",
                    "Invalid parameters"
            );
        }
    }

    @Override
    public ApiResponse getAllAddresses(Integer page, Integer size) {
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Address> addresses = addressRepository.findAll(pageable);
            List<Object> response = new ArrayList<>();
            for (Address address : addresses) {
                response.add(addressMapper.toDto(address));
            }
            return ApiResponse.builder()
                    .message("All addresses found")
                    .status(HttpStatus.OK.getReasonPhrase())
                    .statusCode(HttpStatus.OK.value())
                    .timestamp(LocalDateTime.now())
                    .page(page)
                    .size(size)
                    .data(response)
                    .build();
        } else {
            List<Address> addresses = addressRepository.findAll();
            List<Object> response = new ArrayList<>();
            for (Address address : addresses) {
                response.add(addressMapper.toDto(address));
            }
            return ApiResponse.builder()
                    .message("All addresses found")
                    .status(HttpStatus.OK.getReasonPhrase())
                    .statusCode(HttpStatus.OK.value())
                    .timestamp(LocalDateTime.now())
                    .data(response)
                    .build();
        }
    }
}
