package com.ataya.company.service;

import com.ataya.company.dto.worker.request.UpdateWorkerRequest;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.model.Worker;
import com.ataya.company.util.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WorkerService {
    Worker getWorkerById(String id);

    void saveWorker(Worker worker);

    ApiResponse<WorkerInfoResponse> updateWorker(String id, UpdateWorkerRequest updateWorkerRequest, MultipartFile profilePicture);

    ApiResponse<WorkerInfoResponse> getWorker(String id);

    ApiResponse<List<WorkerInfoResponse>> getWorkers(String name,String surname, String username, String email, String phone,String companyId, String storeId,boolean isUserAdmin, int page, int size);
}
