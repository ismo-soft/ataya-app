package com.ataya.company.dto.store.response;

import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreDetailsResponse {
    private StoreInfoResponse store;
    private List<WorkerInfoResponse> workers;
}
