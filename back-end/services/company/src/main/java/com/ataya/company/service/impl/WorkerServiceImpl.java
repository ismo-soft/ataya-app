package com.ataya.company.service.impl;

import com.ataya.company.dto.worker.request.UpdateWorkerRequest;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.enums.Role;
import com.ataya.company.exception.custom.ResourceNotFoundException;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.WorkerRepository;
import com.ataya.company.service.FileService;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ataya.company.service.impl.CommonService.addCriteria;

@Service
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;

    private final FileService fileService;

    public WorkerServiceImpl(WorkerRepository workerRepository, FileService fileService) {
        this.workerRepository = workerRepository;
        this.fileService = fileService;
    }

    @Override
    public Worker getWorkerById(String id) {
        return workerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "id",
                        id,
                        "Worker with id " + id + " not found"
                )
        );
    }

    @Override
    public void saveWorker(Worker worker) {
        workerRepository.save(worker);
    }

    @Override
    public ApiResponse<WorkerInfoResponse> updateWorker(String id, UpdateWorkerRequest updateWorkerRequest, MultipartFile profilePicture) {
        // get worker by id
        Worker worker = getWorkerById(id);
        List<Role> roles = new ArrayList<>();
        for (String role : updateWorkerRequest.getRoles()) {
            role = role.startsWith("ROLE_") ? role : "ROLE_" + role;
            roles.add(Role.getRole(role));
        }
        // update worker
        String profilePicturePath = fileService.saveImageFile(profilePicture, "worker", "profile", worker.getId());
        worker.setName(updateWorkerRequest.getName());
        worker.setSurname(updateWorkerRequest.getSurname());
        worker.setProfilePicture(profilePicturePath);
        worker.getRoles().clear();
        worker.getRoles().addAll(roles);
        workerRepository.save(worker);
        return ApiResponse.<WorkerInfoResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Worker updated successfully")
                .timestamp(LocalDateTime.now())
                .data(WorkerInfoResponse.builder()
                        .id(worker.getId())
                        .name(worker.getName())
                        .surname(worker.getSurname())
                        .username(worker.getUsername())
                        .email(worker.getEmail())
                        .phone(worker.getPhoneNumber())
                        .profilePicture(worker.getProfilePicture())
                        .companyId(worker.getCompanyId())
                        .storeId(worker.getStoreId())
                        .roles(worker.getRoles().stream().map(Role::name).toList())
                        .build())
                .build();
    }

    @Override
    public ApiResponse<WorkerInfoResponse> getWorker(String id) {
        Worker worker = getWorkerById(id);
        return ApiResponse.<WorkerInfoResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Worker retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(WorkerInfoResponse.builder()
                        .id(worker.getId())
                        .name(worker.getName())
                        .surname(worker.getSurname())
                        .username(worker.getUsername())
                        .email(worker.getEmail())
                        .phone(worker.getPhoneNumber())
                        .profilePicture(worker.getProfilePicture())
                        .companyId(worker.getCompanyId())
                        .storeId(worker.getStoreId())
                        .roles(worker.getRoles().stream().map(Role::name).toList())
                        .build())
                .build();
    }

    @Override
    public ApiResponse<List<WorkerInfoResponse>> getWorkers(
            String name,
            String surname,
            String username,
            String email,
            String phone,
            String companyId,
            String storeId,
            boolean isUserAdmin,
            int page,
            int size
    ) {
        List<Criteria> criteria = buildCriteria(name, surname, username, email, phone, companyId, storeId, isUserAdmin);

        Query query = new Query();
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        long total = workerRepository.countWorkersByQuery(query);
        if (size <= 0) {
            size = total == 0 ? 1 : (int) total;
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        query.with(pageRequest);
        List<Worker> workers = workerRepository.findWorkersByQuery(query);

        return buildWorkerInfoResponse(workers, total, page, size);
    }

    private List<Criteria> buildCriteria(
            String name,
            String surname,
            String username,
            String email,
            String phone,
            String companyId,
            String storeId,
            boolean isUserAdmin
    ) {
        List<Criteria> criteria = new ArrayList<>();
        addCriteria(criteria, "name", name);
        addCriteria(criteria, "surname", surname);
        addCriteria(criteria, "username", username);
        addCriteria(criteria, "email", email);
        addCriteria(criteria, "phoneNumber", phone);

        if (companyId != null && !companyId.isEmpty()) {
            criteria.add(Criteria.where("companyId").is(companyId));
        }
        if (storeId != null && !storeId.isEmpty()) {
            List<String> storeIds = isUserAdmin ? List.of(storeId.split(",")) : List.of(storeId);
            criteria.add(Criteria.where("storeId").in(storeIds));
        }

        return criteria;
    }



    private ApiResponse<List<WorkerInfoResponse>> buildWorkerInfoResponse(List<Worker> workers, long total, int page, int size) {
        return ApiResponse.<List<WorkerInfoResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .message("Workers retrieved successfully")
                .timestamp(LocalDateTime.now())
                .data(workers.stream().map(worker -> WorkerInfoResponse.builder()
                        .id(worker.getId())
                        .name(worker.getName())
                        .surname(worker.getSurname())
                        .username(worker.getUsername())
                        .email(worker.getEmail())
                        .phone(worker.getPhoneNumber())
                        .profilePicture(worker.getProfilePicture())
                        .companyId(worker.getCompanyId())
                        .storeId(worker.getStoreId())
                        .roles(worker.getRoles().stream().map(Role::name).toList())
                        .build()).toList())
                .total(total)
                .page(page)
                .size(size)
                .totalPages((int) Math.ceil((double) total / size))
                .build();
    }
}
