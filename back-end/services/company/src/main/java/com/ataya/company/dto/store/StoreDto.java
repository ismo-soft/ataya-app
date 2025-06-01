package com.ataya.company.dto.store;

import com.ataya.company.enums.StoreStatus;
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
    private StoreStatus status;
    private String addressId;
}
