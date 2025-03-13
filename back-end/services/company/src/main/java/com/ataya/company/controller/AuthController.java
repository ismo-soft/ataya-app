package com.ataya.company.controller;


import com.ataya.company.dto.worker.request.*;
import com.ataya.company.service.AuthService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // register
    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Register a new user with the given information"
    )
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
    }

    // login
    @PostMapping("/login")
    @Operation(
            summary = "Login",
            description = "Login with the given information"
    )
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    // forgot password
    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot password",
            description = "Send email to reset password"
    )
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.forgotPassword(email));
    }

    // reset password
    @PostMapping("/reset-password")
    @Operation(
            summary = "Reset password",
            description = "Reset password with the given information. username, email and id one of them is enough to reset password."
    )
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String token,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestBody ResetPasswordRequest password
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.resetPassword(token, id, username, email, password));
    }


    // change password
    @PostMapping("/change-password")
    @Operation(
            summary = "Change password",
            description = "Change password with the given information"
    )
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') or " +
            "#changePasswordRequest.id == authentication.principal.id"
    )
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.changePassword(changePasswordRequest));
    }

    // change email
    @PostMapping("/change-email")
    @Operation(
            summary = "Change email",
            description = "Change email with the given information"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ROLE_ADMIN')" +
                    " or " +
                    "#changeEmailRequest.id == authentication.principal.id" +
            ") " +
            "and " +
            "@authServiceImpl.isPasswordMatched(" +
                    "#changeEmailRequest.id, " +
                    "#changeEmailRequest.password" +
            ")"
    )
    public ResponseEntity<ApiResponse> changeEmail(@RequestBody ChangeEmailRequest changeEmailRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.changeEmail(changeEmailRequest));
    }

    // verify email token
    @PostMapping("/verify-email")
    @Operation(
            summary = "Verify email",
            description = "email, id and username one of them is enough to verify email."
    )
    public ResponseEntity<ApiResponse> verifyEmail(
            @RequestParam String token,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyEmail(token, email, id, username));
    }


    // change username
    @PostMapping("/change-username")
    @Operation(
            summary = "Change username",
            description = "Change username with the given information"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ROLE_ADMIN') " +
                    "or " +
                    "#changeUsernameRequest.id == authentication.principal.id" +
            ") " +
            "and " +
            "@authServiceImpl.isPasswordMatched(" +
                    "#changeUsernameRequest.id, " +
                    "#changeUsernameRequest.password" +
            ")"
    )
    public ResponseEntity<ApiResponse> changeUsername(@RequestBody ChangeUsernameRequest changeUsernameRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.changeUsername(changeUsernameRequest));
    }


    // change phone number
    @PostMapping("/change-phone")
    @Operation(
            summary = "Change phone number",
            description = "Change phone number with the given information"
    )
    @PreAuthorize(
            "(" +
                    "hasRole('ROLE_ADMIN') " +
                    "or " +
                    "#changePhoneRequest.id == authentication.principal.id" +
            ") " +
            "and " +
            "@authServiceImpl.isPasswordMatched(" +
                    "#changePhoneRequest.id, " +
                    "#changePhoneRequest.password" +
            ")"
    )
    public ResponseEntity<ApiResponse> changePhone(@RequestBody ChangePhoneRequest changePhoneRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.changePhone(changePhoneRequest));
    }

    // register low level worker
    @PostMapping("/register-worker")
    @Operation(
            summary = "Register a new worker",
            description = "Register a new worker with base information and lowest role"
    )
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') " +
                    "or" +
                    "(" +
                        "hasRole('ROLE_MANAGER') " +
                        "and " +
                        "@workerServiceImpl.isManagerOfStore(" +
                            "authentication.principal.id," +
                            "#registerWorkerRequest.storeId" +
                        ")" +
                    ")"
    )
    public ResponseEntity<ApiResponse> registerWorker(@RequestBody RegisterWorkerRequest registerWorkerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerWorker(registerWorkerRequest));
    }
}
