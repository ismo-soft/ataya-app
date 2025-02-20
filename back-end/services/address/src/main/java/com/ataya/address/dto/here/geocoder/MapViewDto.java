package com.ataya.address.dto.here.geocoder;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapViewDto {
    private Double west;
    private Double south;
    private Double east;
    private Double north;
}
