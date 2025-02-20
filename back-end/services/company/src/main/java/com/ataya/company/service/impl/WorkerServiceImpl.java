package com.ataya.company.service.impl;

import com.ataya.company.dto.worker.request.UpdateWorkerRequest;
import com.ataya.company.dto.worker.response.WorkerDetailsResponse;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.mapper.WorkerMapper;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.WorkerRepository;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WorkerServiceImpl implements WorkerService {
    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerMapper workerMapper;

    @Override
    public boolean isManagerOfStore(String userId, String storeId) {
        // get the storeId of the worker
        String storeIdOfWorker = workerRepository.getStoreIdOfWorker(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Worker",
                                "id/storeId",
                                userId + "/" + storeId
                        )
                );
        return storeIdOfWorker.equals(storeId);
    }

    @Override
    public ApiResponse getWorkerById(String id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", id)
                );
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker,WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse getWorkerByUsername(String username) {
        Worker worker = workerRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "username", username)
                );
        Object o = workerMapper.workerToWorkerDto(worker,WorkerDetailsResponse.class);
        System.out.println(o);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker retrieved successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        o
                )
                .build();
    }

    @Override
    public ApiResponse updateWorker(String id, UpdateWorkerRequest updateWorkerRequest) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", id)
                );
        worker.setName(updateWorkerRequest.getName());
        worker.setSurname(updateWorkerRequest.getSurname());
        worker.setProfilePicture(updateWorkerRequest.getProfilePicture());
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker updated successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker,WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public Worker getWorkerEntityByUsername(String username) {
        return workerRepository.findByUsername(username)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "username", username)
                );
    }

    @Override
    public void setCompany(String workerId, String companyId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", workerId)
                );
        worker.setCompanyId(companyId);
        workerRepository.save(worker);
    }

    public boolean getIsCompanyOwner(String companyId, String userId) {
        Worker worker = workerRepository.findById(userId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", userId)
                );
        return worker.getCompanyId().equals(companyId);

    }
}
