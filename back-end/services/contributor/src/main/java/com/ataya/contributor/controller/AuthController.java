package com.ataya.contributor.controller;

import com.ataya.contributor.dto.user.ContributorDto;
import com.ataya.contributor.dto.user.CredentialRequest;
import com.ataya.contributor.dto.user.LoginResponse;
import com.ataya.contributor.service.AuthService;
import com.ataya.contributor.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    // register user
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<ContributorDto>> registerUser(@RequestBody CredentialRequest request) {
        return ResponseEntity.status(201).body(authService.registerUser(request));
    }

    // login user
    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(@RequestBody CredentialRequest request) {
        return ResponseEntity.ok(authService.loginUser(request));
    }

    // update user
    @PutMapping("/update")
    @Operation(summary = "Update user details")
    public ResponseEntity<ApiResponse<ContributorDto>> updateUser(@RequestBody ContributorDto contributorDto, @AuthenticationPrincipal String id) {
        return ResponseEntity.ok(authService.updateUser(id, contributorDto));
    }

    // get user profile
     @GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public ResponseEntity<ApiResponse<ContributorDto>> getUserProfile(@AuthenticationPrincipal String id) {
        return ResponseEntity.ok(authService.getUserProfile(id));

    }



}
