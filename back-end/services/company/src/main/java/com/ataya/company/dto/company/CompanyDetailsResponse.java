package com.ataya.company.dto.company;

import com.ataya.company.dto.product.ProductInfoResponse;
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
    private long workerCount;
    private List<StoreInfoResponse> stores;
    private long storeCount;
    private List<ProductInfoResponse> products;
    private long productCount;
}
