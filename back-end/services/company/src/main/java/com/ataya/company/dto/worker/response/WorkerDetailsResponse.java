package com.ataya.company.dto.worker.response;

import com.ataya.company.enums.Role;
import lombok.*;

import java.util.List;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WorkerDetailsResponse {
    private String id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String phone;
    private String storeId;
    private String companyId;
    private String managerId;
    private String profilePicture;
}
