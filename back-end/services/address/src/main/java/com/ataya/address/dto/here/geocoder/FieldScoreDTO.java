package com.ataya.address.dto.here.geocoder;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FieldScoreDTO {
    private Double country;
    private Double city;
    private List<Double> streets;
    private Integer houseNumber;
    private Double postalCode;
}
