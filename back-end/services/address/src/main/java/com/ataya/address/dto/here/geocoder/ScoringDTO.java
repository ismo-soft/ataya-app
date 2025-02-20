package com.ataya.address.dto.here.geocoder;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoringDTO {
    private Double queryScore;
    private FieldScoreDTO fieldScore;
}
