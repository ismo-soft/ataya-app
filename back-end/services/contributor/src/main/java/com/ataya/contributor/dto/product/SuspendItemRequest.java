package com.ataya.contributor.dto.product;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuspendItemRequest {

    private String itemId;
    private Double quantity;

}
