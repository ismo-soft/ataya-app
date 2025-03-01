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
    @PutMapping("/{id}/set-store/{storeId}")
    @Operation(
            summary = "Set worker store",
            description = "Set worker store"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> setWorkerStore(@PathVariable String id, @PathVariable String storeId) {
        return ResponseEntity.ok(workerService.setWorkerStore(id, storeId));
    }

    @PutMapping("/{id}/set-company/{companyId}")
    @Operation(
            summary = "Set worker company",
            description = "Set worker company"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> setWorkerCompany(@PathVariable String id, @PathVariable String companyId) {
        return ResponseEntity.ok(workerService.setWorkerCompany(id, companyId));
    }

    @PutMapping("/{id}/set-manager/{managerId}")
    @Operation(
            summary = "Set worker manager",
            description = "Set worker manager"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> setWorkerManager(@PathVariable String id, @PathVariable String managerId) {
        return ResponseEntity.ok(workerService.setWorkerManager(id, managerId));
    }

    @PutMapping("/{id}/upgrade-manager")
    @Operation(
            summary = "Upgrade worker to manager",
            description = "Upgrade worker to manager"
    )
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') and " +
            "@workerServiceImpl.isWorkerOfUserCompany(#id,authentication.principal.companyId)"
    )
    public ResponseEntity<ApiResponse> upgradeWorkerToManager(@PathVariable String id) {
        return ResponseEntity.ok(workerService.upgradeWorkerToManager(id));
    }


    @PutMapping("/{id}/upgrade-admin")
    @Operation(
            summary = "Upgrade manager to admin",
            description = "Upgrade manager to admin"
    )
    @PreAuthorize(
            "hasRole('ROLE_SUPER_ADMIN') and " +
            "@workerServiceImpl.isWorkerOfUserCompany(#id,authentication.principal.companyId)"
    )
    public ResponseEntity<ApiResponse> upgradeManagerToAdmin(@PathVariable String id) {
        return ResponseEntity.ok(workerService.upgradeManagerToAdmin(id));
    }

    @PutMapping("/{id}/downgrade-admin")
    @Operation(
            summary = "Downgrade admin to manager",
            description = "Downgrade admin to manager"
    )
    @PreAuthorize(
            "hasRole('ROLE_SUPER_ADMIN') and " +
            "@workerServiceImpl.isWorkerOfUserCompany(#id,authentication.principal.companyId)"
    )
    public ResponseEntity<ApiResponse> downgradeAdminToManager(@PathVariable String id) {
        return ResponseEntity.ok(workerService.downgradeAdminToManager(id));
    }

    @PutMapping("/{id}/downgrade-manager")
    @Operation(
            summary = "Downgrade manager to worker",
            description = "Downgrade manager to worker"
    )
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') and " +
            "@workerServiceImpl.isWorkerOfUserCompany(#id,authentication.principal.companyId)"
    )
    public ResponseEntity<ApiResponse> downgradeManagerToWorker(@PathVariable String id) {
        return ResponseEntity.ok(workerService.downgradeManagerToWorker(id));
    }

    @PutMapping("/{id}/lock")
    @Operation(
            summary = "Lock worker",
            description = "Lock worker"
    )
    @PreAuthorize(
            "hasRole('ROLE_SUPER_ADMIN') and " +
            "@workerServiceImpl.isWorkerOfUserCompany(#id,authentication.principal.companyId)"
    )
    public ResponseEntity<ApiResponse> lockWorker(@PathVariable String id) {
        return ResponseEntity.ok(workerService.lockWorker(id));
    }
    @PutMapping("/{id}/unlock")
    @Operation(
            summary = "Unlock worker",
            description = "Unlock worker"
    )
    @PreAuthorize(
            "hasRole('ROLE_SUPER_ADMIN') and " +
            "@workerServiceImpl.isWorkerOfUserCompany(#id,authentication.principal.companyId)"
    )
    public ResponseEntity<ApiResponse> unlockWorker(@PathVariable String id) {
        return ResponseEntity.ok(workerService.unlockWorker(id));
    }
}
