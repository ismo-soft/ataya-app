package com.ataya.contributor.dto.store;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {
    private String id;
    private String name;
    private String profilePicture;
    private String email;
    private String phoneNumber;
    private String status;
    private String addressId;
}
