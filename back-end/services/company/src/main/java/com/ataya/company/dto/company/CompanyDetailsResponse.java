package com.ataya.company.dto.company;

import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDetailsResponse {
    private CompanyInfoResponse company;
    private List<WorkerInfoResponse> workers;
    private List<StoreInfoResponse> stores;
}
