package com.ataya.inventory.dto.contributor;


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
