package com.ataya.company.service.impl;

import com.ataya.company.dto.worker.request.UpdateWorkerRequest;
import com.ataya.company.dto.worker.response.WorkerDetailsResponse;
import com.ataya.company.enums.Role;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.exception.Custom.ValidationException;
import com.ataya.company.mapper.WorkerMapper;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.WorkerRepository;
import com.ataya.company.service.StoreService;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerService {
    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    private StoreService storeService;

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
    public void setCompany(String workerId, String companyId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", workerId)
                );
        worker.setCompanyId(companyId);
        workerRepository.save(worker);
    }

    @Override
    public List<Object> getAllWorkersOfCompany(String companyId) {
        List<Worker> workers = workerRepository.findAllByCompanyId(companyId);
        List<Object> response = new ArrayList<>();
        for (Worker worker : workers) {
            response.add(
                    workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
            );
        }
        return response;
    }

    @Override
    public List<Object> getAllManagersOfCompany(String companyId) {
        List<Worker> workers = workerRepository.findAllByCompanyIdAndRolesContains(companyId, Role.ROLE_MANAGER);
        List<Object> response = new ArrayList<>();
        for (Worker worker : workers) {
            response.add(
                    workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
            );
        }
        return response;
    }

    @Override
    public ApiResponse setWorkerStore(String workerId, String storeId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", workerId)
                );
        if (worker.getCompanyId() == null) {
            throw new ValidationException(
                    "id",
                    workerId,
                    "Worker does not have a company"
            );
        }
        if(!storeService.isStoreBelongToCompany(storeId,worker.getCompanyId())){
            throw new ValidationException(
                    "storeId",
                    storeId,
                    "Store does not belong to the company the worker is in"
            );
        }
        worker.setStoreId(storeId);
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker store set successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse setWorkerCompany(String workerId, String companyId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", workerId)
                );
        worker.setCompanyId(companyId);
        worker.setStoreId(null);
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker company set successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
                )
                .build();
    }

    // TODO: Implement this method
    @Override
    public ApiResponse setWorkerManager(String workerId, String managerId) {
        return null;
    }

    @Override
    public ApiResponse upgradeManagerToAdmin(String id) {
        // get the worker
        Worker worker = workerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", id)
                );
        // check if the worker is in a company
        if (worker.getCompanyId() == null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker does not have a company"
            );
        }

        // check if the worker is a manager
        if (!worker.getRoles().contains(Role.ROLE_MANAGER)) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker is not a manager"
            );
        }
        // upgrade the worker to admin
        worker.getRoles().add(Role.ROLE_ADMIN);
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker upgraded to admin successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse downgradeAdminToManager(String id) {
        // get the worker
        Worker worker = workerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", id)
                );
        // check if the worker is in a company
        if (worker.getCompanyId() == null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker does not have a company"
            );
        }
        // check if the worker is in store
        if (worker.getStoreId() != null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker is in a store"
            );
        }
        // check if the worker is an admin
        if (!worker.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker is not an admin"
            );
        }
        // downgrade the worker to manager
        worker.getRoles().remove(Role.ROLE_ADMIN);
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker downgraded to manager successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse downgradeManagerToWorker(String id) {
        // get the worker
        Worker worker = workerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", id)
                );
        // check if the worker is in a company
        if (worker.getCompanyId() == null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker does not have a company"
            );
        }
        // check if the worker is in store
        if (worker.getStoreId() != null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker is in a store"
            );
        }
        // check if the worker is a manager
        if (!worker.getRoles().contains(Role.ROLE_MANAGER)) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker is not a manager"
            );
        }
        // downgrade the worker to worker
        worker.getRoles().remove(Role.ROLE_MANAGER);
        // if worker is an admin, downgrade to worker
        worker.getRoles().remove(Role.ROLE_ADMIN);
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker downgraded to worker successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse upgradeWorkerToManager(String id) {
        // get the worker
        Worker worker = workerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", id)
                );
        // check if the worker is in a company
        if (worker.getCompanyId() == null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker does not have a company"
            );
        }
        // check if the worker is in store
        if (worker.getStoreId() != null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker is in a store"
            );
        }
        // check if the worker is a worker
        if (!worker.getRoles().contains(Role.ROLE_WORKER)) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker is not a worker"
            );
        }
        // upgrade the worker to manager
        worker.getRoles().add(Role.ROLE_MANAGER);
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker upgraded to manager successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse lockWorker(String id) {
        // get the worker
        Worker worker = workerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", id)
                );
        // check if the user is in a company and store
        if (worker.getCompanyId() == null || worker.getStoreId() == null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker does not have a company or store"
            );
        }
        // lock the worker
        worker.setAccountNonLocked(false);
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker locked successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public ApiResponse unlockWorker(String id) {
        // get the worker
        Worker worker = workerRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", id)
                );
        // check if the user is in a company and store
        if (worker.getCompanyId() == null || worker.getStoreId() == null) {
            throw new ValidationException(
                    "id",
                    id,
                    "Worker does not have a company or store"
            );
        }
        // unlock the worker
        worker.setAccountNonLocked(true);
        workerRepository.save(worker);
        return ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("Worker unlocked successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
                )
                .build();
    }

    @Override
    public List<Object> getStoreWorkers(String companyId, String storeId) {
        List<Worker> workers = workerRepository.findAllByCompanyIdAndStoreId(companyId, storeId);
        List<Object> response = new ArrayList<>();
        for (Worker worker : workers) {
            response.add(
                    workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
            );
        }
        return response;
    }

    @Override
    public List<Object> getStoreManagers(String companyId, String storeId) {
        List<Worker> workers = workerRepository.findAllByCompanyIdAndStoreIdAndRolesContains(companyId, storeId, Role.ROLE_MANAGER);
        List<Object> response = new ArrayList<>();
        for (Worker worker : workers) {
            response.add(
                    workerMapper.workerToWorkerDto(worker, WorkerDetailsResponse.class)
            );
        }
        return response;
    }

    @Override
    public boolean isStoreWorker(String storeId, String workerId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", workerId)
                );
        return worker.getStoreId().equals(storeId);
    }

    @Override
    public boolean hasRole(String workerId, String roleManager) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", workerId)
                );
        return worker.getRoles().contains(Role.valueOf(roleManager));
    }

    public boolean isWorkerOfUserCompany(String workerId, String companyId) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Worker", "id", workerId)
                );
        return worker.getCompanyId().equals(companyId);
    }
}
