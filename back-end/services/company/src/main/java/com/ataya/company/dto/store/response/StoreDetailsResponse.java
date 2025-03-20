package com.ataya.company.dto.store.response;

import com.ataya.company.dto.product.ProductInfoResponse;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreDetailsResponse {
    private StoreInfoResponse store;
    private List<WorkerInfoResponse> workers;
    private long workerCount;
    private List<ProductInfoResponse> products;
    private long productCount;
}
