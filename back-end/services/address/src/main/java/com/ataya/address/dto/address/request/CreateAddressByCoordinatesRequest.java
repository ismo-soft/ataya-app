package com.ataya.address.dto.address.request;

import com.ataya.address.enums.AddressTag;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAddressByCoordinatesRequest {
    private Double lat;
    private Double lng;
    private String addressDetails;
    private List<String> addressTags;
}
