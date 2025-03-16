package com.ataya.company.dto.store.request;

import com.ataya.company.enums.StoreStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateStoreRequest {
    @NotNull(message = "name is required")
    @NotEmpty(message = "can not be empty")
    private String name;
    private String storeCode;
    private String description;
    private String profilePicture;
    private String email;
    private String phoneNumber;
    private String website;
    private Map<String, String> socialMedia;
    private StoreStatus status;
    private String addressId;
}
