package com.ataya.address.dto.here.geocoder;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionDTO {
    private Double lat;
    private Double lng;
}
