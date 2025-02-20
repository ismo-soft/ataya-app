package com.ataya.address.dto.here.geocoder;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HereGeocoderResponse {
    private List<AddressItemDTO> items;
}
