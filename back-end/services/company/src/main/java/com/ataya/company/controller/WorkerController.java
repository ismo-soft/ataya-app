package com.ataya.company.controller;

import com.ataya.company.dto.worker.request.UpdateWorkerRequest;
import com.ataya.company.service.WorkerService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company/worker")
public class WorkerController {


    @Autowired
    private WorkerService workerService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get worker by id",
            description = "Get worker by id"
    )
    public ResponseEntity<ApiResponse> getWorkerById(@PathVariable String id) {
        return ResponseEntity.ok(workerService.getWorkerById(id));
    }
    // get profile of worker by username
    @GetMapping("/username/{username}")
    @Operation(
            summary = "Get worker by username",
            description = "Get worker by username"
    )
    public ResponseEntity<ApiResponse> getWorkerByUsername(@PathVariable String username) {
        return ResponseEntity.ok(workerService.getWorkerByUsername(username));
    }

    // update profile of worker
    @PutMapping("/{id}")
    @Operation(
            summary = "Update worker",
            description = "Update worker"
    )
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> updateWorker(@PathVariable String id, @RequestBody UpdateWorkerRequest updateWorkerRequest) {
        return ResponseEntity.ok(workerService.updateWorker(id, updateWorkerRequest));
    }
}
