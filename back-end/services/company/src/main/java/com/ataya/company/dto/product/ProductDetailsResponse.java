package com.ataya.company.dto.product;

import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor@NoArgsConstructor
@Builder
public class ProductDetailsResponse {
    private ProductInfoResponse info;
    private CompanyInfoResponse company;
    private WorkerInfoResponse createdBy;
    private WorkerInfoResponse updatedBy;
}
