package com.ataya.company.controller;


import com.ataya.company.dto.worker.request.UpdateWorkerRequest;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.enums.Role;
import com.ataya.company.model.Worker;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/worker")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    // get worker by id
    @GetMapping("/{id}")
    @PreAuthorize(
            "(hasRole('ADMIN') and @workerSecurity.hasAccess(#id, authentication.principal.companyId,null)) or" +
            "(hasRole('MANAGER') and @workerSecurity.hasAccess(#id, null,authentication.principal.storeId)) or" +
            "(hasRole('WORKER') and #id == authentication.principal.id)"
    )
    @Operation(
            summary = "Get worker by id",
            description = """
                    This endpoint is used to get worker by id. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: \s
                        - user with role ADMIN can get worker of his company. \s
                        - user with role MANAGER can get worker of store where he is manager. \s
                        - user with role WORKER can get his own information. \s
                    """
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> getWorkerById(@PathVariable String id) {
        return ResponseEntity.status(200).body(workerService.getWorker(id));
    }

    // update worker
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize(
            "#id == authentication.principal.id"
    )
    @Operation(
            summary = "Update worker",
            description = """
                    This endpoint is used to update worker. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user can update his own information. \s
                    """
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> updateWorker(@PathVariable String id, @RequestPart UpdateWorkerRequest updateWorkerRequest, @RequestPart(required = false,value = "profilePicture") MultipartFile profilePicture) {
        return ResponseEntity.status(200).body(workerService.updateWorker(id, updateWorkerRequest, profilePicture));
    }

    // get workers
    @GetMapping("/workers")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(
            summary = "Get workers",
            description = """
                    This endpoint is used to get workers. \s
                    - Filter workers by request parameters \s
                    - Request parameters:
                        - nm: name of worker
                        - snm: surname of worker
                        - unm: username of worker
                        - eml: email of worker
                        - phn: phone number of worker
                        - strId: store id of worker
                    
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: \s
                        - user with role ADMIN can get workers of his company. \s
                        - user with role MANAGER can get workers of store where he is manager. \s
                    """
    )
    public ResponseEntity<ApiResponse<List<WorkerInfoResponse>>> getWorkers(
            @RequestParam(required = false, name = "nm") String name,
            @RequestParam(required = false, name = "snm") String surname,
            @RequestParam(required = false, name = "unm") String username,
            @RequestParam(required = false, name = "eml") String email,
            @RequestParam(required = false, name = "phn") String phone,
            @RequestParam(required = false, name = "strId") String storeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Worker authenticatedPrincipal
    ) {
        if (authenticatedPrincipal.getRoles().contains(Role.ROLE_ADMIN)) {
            return ResponseEntity.status(200).body(workerService.getWorkers(name,surname,username,email,phone,authenticatedPrincipal.getCompanyId(),storeId,authenticatedPrincipal.getRoles().contains(Role.ROLE_ADMIN),page,size));
        } else if (authenticatedPrincipal.getRoles().contains(Role.ROLE_MANAGER)) {
            return ResponseEntity.status(200).body(workerService.getWorkers(name,surname,username,email,phone,authenticatedPrincipal.getCompanyId(),authenticatedPrincipal.getStoreId(),authenticatedPrincipal.getRoles().contains(Role.ROLE_ADMIN),page,size));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
