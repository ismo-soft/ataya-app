package com.ataya.company.controller;

import com.ataya.company.model.Worker;
import com.ataya.company.service.CompanyService;
import com.ataya.company.service.impl.CompanyServiceImpl;
import com.ataya.company.service.impl.WorkerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company/debug")
public class DebugController {

    @Autowired
    private WorkerServiceImpl workerService;

    @GetMapping("/debug/roles")
    @PreAuthorize("isAuthenticated()")
    public List<String> debugRoles(@AuthenticationPrincipal Worker worker) {
        return worker.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
    @GetMapping("/debug/principal")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> debugPrincipal(@AuthenticationPrincipal Worker worker) {
        return Map.of(
                "id", worker.getId(),
                "username", worker.getUsername(),
                "roles", worker.getRoles()
        );
    }
    // endpoint only for admin
    @GetMapping("/debug/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String debugAdmin() {
        return "Hello admin";
    }
    // endpoint only for super admin
    @GetMapping("/debug/super-admin")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String debugSuperAdmin() {
        return "Hello super admin";
    }
    // endpoint only for worker
    @GetMapping("/debug/worker")
    @PreAuthorize("hasRole('ROLE_WORKER')")
    public String debugWorker() {
        return "Hello worker";
    }
    // endpoint only for manager
    @GetMapping("/debug/manager")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String debugManager() {
        return "Hello manager";
    }

}
