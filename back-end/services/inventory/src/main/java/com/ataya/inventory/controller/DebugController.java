package com.ataya.inventory.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/debug")
public class DebugController {

    // endpoint to say hello to all users
    @GetMapping("/hello")
    @PreAuthorize("permitAll()")
    public String hello() {
        return "Hello from DebugController";
    }

    // endpoint to say hello to admin users
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String helloAdmin() {
        return "Hello from DebugController for Admin";
    }

    // endpoint to say hello to super admin users
    @GetMapping("/super-admin")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public String helloSuperAdmin() {
        return "Hello from DebugController for Super Admin";
    }

    // endpoint to say hello to manager users
    @GetMapping("/manager")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public String helloCeo() {
        return "Hello from DebugController for Manager";
    }

    // endpoint to say hello to worker users
    @GetMapping("/worker")
    @PreAuthorize("hasRole('ROLE_WORKER')")
    public String helloWorker() {
        return "Hello from DebugController for Worker";
    }

    // whoami endpoint
    @GetMapping("/whoami")
    public String whoami(@AuthenticationPrincipal String username) {
        return "Hello " + username;
    }

    // public endpoint to get all roles
    @GetMapping("/public/hi")
    public String getAllRoles() {
        return "Hi There";
    }
}
