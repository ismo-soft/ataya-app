package com.ataya.company.service;

import com.ataya.company.dto.worker.request.UpdateWorkerRequest;
import com.ataya.company.model.Worker;
import com.ataya.company.util.ApiResponse;

public interface WorkerService {

    boolean isManagerOfStore(String userId, String storeId);

    ApiResponse getWorkerById(String id);

    ApiResponse getWorkerByUsername(String username);

    ApiResponse updateWorker(String id, UpdateWorkerRequest updateWorkerRequest);

    Worker getWorkerEntityByUsername(String username);

    void setCompany(String workerId, String companyId);
}
