package com.ataya.company.service;

import com.ataya.company.dto.worker.request.UpdateWorkerRequest;
import com.ataya.company.dto.worker.response.WorkerDetailsResponse;
import com.ataya.company.model.Worker;
import com.ataya.company.util.ApiResponse;

import java.util.List;

public interface WorkerService {

    boolean isManagerOfStore(String userId, String storeId);

    ApiResponse getWorkerById(String id);

    ApiResponse getWorkerByUsername(String username);

    ApiResponse updateWorker(String id, UpdateWorkerRequest updateWorkerRequest);

    void setCompany(String workerId, String companyId);

    List<Object> getAllWorkersOfCompany(String companyId);

    List<Object> getAllManagersOfCompany(String companyId);

    ApiResponse setWorkerStore(String workerId, String storeId);

    ApiResponse setWorkerCompany(String workerId, String companyId);

    ApiResponse setWorkerManager(String workerId, String managerId);

    ApiResponse upgradeManagerToAdmin(String id);

    ApiResponse downgradeAdminToManager(String id);

    ApiResponse downgradeManagerToWorker(String id);

    ApiResponse upgradeWorkerToManager(String id);

    ApiResponse lockWorker(String id);

    ApiResponse unlockWorker(String id);
}
