package com.ataya.address.dto.here.geocoder;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressItemDTO {
    private String title;
    private String id;
    private String resultType;
    private String houseNumberType;
    private AddressDTO address;
    private PositionDTO position;
    private List<PositionDTO> access;
    private MapViewDto mapView;
    private ScoringDTO scoring;
}
