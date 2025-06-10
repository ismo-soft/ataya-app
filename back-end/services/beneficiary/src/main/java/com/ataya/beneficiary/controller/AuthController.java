package com.ataya.beneficiary.controller;

import com.ataya.beneficiary.dto.beneficiary.BeneficiaryDto;
import com.ataya.beneficiary.dto.beneficiary.CredentialRequest;
import com.ataya.beneficiary.dto.beneficiary.LoginResponse;
import com.ataya.beneficiary.model.Beneficiary;
import com.ataya.beneficiary.service.AuthService;
import com.ataya.beneficiary.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // register user
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<BeneficiaryDto>> registerUser(@RequestBody CredentialRequest request) {
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
    public ResponseEntity<ApiResponse<BeneficiaryDto>> updateUser(@RequestBody BeneficiaryDto contributorDto, @AuthenticationPrincipal Beneficiary user) {
        return ResponseEntity.ok(authService.updateUser(user.getId(), contributorDto));
    }

    // get user profile
    @GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public ResponseEntity<ApiResponse<BeneficiaryDto>> getUserProfile(@AuthenticationPrincipal Beneficiary user) {
        return ResponseEntity.ok(authService.getUserProfile(user.getId()));

    }

}
