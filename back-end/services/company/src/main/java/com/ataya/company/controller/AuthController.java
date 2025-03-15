package com.ataya.company.controller;


import com.ataya.company.dto.worker.request.*;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.model.Worker;
import com.ataya.company.service.AuthService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // register
    @PostMapping("/register")
    @Operation(
            summary = "1. Register a new user",
            description = """
                    Use this endpoint to register as new user to the system.  \s
                    Once user registered with this endpoint these roles will get automatically:  \s
                       - ROLE_SUPER_ADMIN,
                       - ROLE_ADMIN,
                       - ROLE_MANAGER,
                       - ROLE_WORKER.

                    Token here is for verification email.  \s
                    ### Authentication: no need to Bearer token.  \s
                    ### Authorization: available to all users.  \s
                    """
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
    }

    // login
    @PostMapping("/login")
    @Operation(
            summary = "3. Login",
            description = """
                    Use this endpoint to login with required credentials.  \s
                    Username or email and password are required.  \s
                    Token will be generated for the user and it will be used for authentication.  \s
                    ### Authentication: no need to Bearer token.  \s
                    ### Authorization: available to all users."""
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequest));
    }

    // forgot password
    @PostMapping("/forgot-password")
    @Operation(
            summary = "a. Forgot password",
            description = """
                    Use this endpoint to reset password with the given email. \s
                    Once user requested to reset password, reset password token will be provided in the response \s
                    ### Authentication: no need to Bearer token. \s
                    ### Authorization: available to all users.
                    """
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> forgotPassword(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.forgotPassword(email));
    }

    // reset password
    @PutMapping("/reset-password")
    @Operation(
            summary = "b. Reset password",
            description = """
                    Use this endpoint to reset password with the given token. \s
                    email, id and username one of them is enough to reset password. \s
                    if you providing 2 or more parameters you should know that email, id then username in order will be used to fetch the user . \s
                    ### Authentication: no need to Bearer token. \s
                    ### Authorization: available to all users.
                    """
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> resetPassword(
            @RequestParam String token,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestBody ResetPasswordRequest password
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.resetPassword(token, id, username, email, password));
    }


    // change password
    @PutMapping("/change-password")
    @Operation(
            summary = "Change password",
            description = """
                    Use this endpoint to change password with the given information. \s
                    This endpoint is for changing password not for resetting password,user have to provide old password. \s
                    ### Authentication: Bearer token required. \s
                    ### Authorization: available authenticated users, user can change only his/her password.
                    """
    )
    @PreAuthorize(
            "#changePasswordRequest.id == authentication.principal.id"
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.changePassword(changePasswordRequest));
    }

    // change email
    @PutMapping("/change-email")
    @Operation(
            summary = "Change email",
            description = """
                    Use this endpoint to change email with the given information. \s
                    This endpoint is for changing email not setting email, user have to provide password. \s
                    Once email changed, user have to verify email with the token sent in the response \s
                    ### Authentication: Bearer token required. \s
                    ### Authorization: available authenticated users, user can change only his/her email.
                    """
    )
    @PreAuthorize(
            "(" +
                    "#changeEmailRequest.id == authentication.principal.id" +
            ") " +
            "and " +
            "@authServiceImpl.isPasswordMatched(" +
                    "#changeEmailRequest.id, " +
                    "#changeEmailRequest.password" +
            ")"
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> changeEmail(@RequestBody ChangeEmailRequest changeEmailRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.changeEmail(changeEmailRequest));
    }

    // verify email token
    @PostMapping("/verify-email")
    @Operation(
            summary = "2. Verify email",
            description = """
                    Use this endpoint to verify email with the given token.  \s
                    Email, id and username one of them with token are enough to verify email.  \s
                    if you providing 2 or more parameters you should know that email, id then username in order will be used to fetch the user .  \s
                    ### Authentication: no need to Bearer token.  \s
                    ### Authorization: available to all users.
                    """
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> verifyEmail(
            @RequestParam String token,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String username
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.verifyEmail(token, email, id, username));
    }


    // change username
    @PutMapping("/change-username")
    @Operation(
            summary = "Change username",
            description = """
                    Use this endpoint to change username with the given information.  \s
                    This endpoint is for changing, user have to provide password.  \s
                    ### Authentication: Bearer token required.  \s
                    ### Authorization: available authenticated users, user can change only his/her username.
                    """
    )
    @PreAuthorize(
            "(" +
                    "#changeUsernameRequest.id == authentication.principal.id" +
            ") " +
            "and " +
            "@authServiceImpl.isPasswordMatched(" +
                    "#changeUsernameRequest.id, " +
                    "#changeUsernameRequest.password" +
            ")"
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> changeUsername(@RequestBody ChangeUsernameRequest changeUsernameRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.changeUsername(changeUsernameRequest));
    }

    // change phone number
    @PutMapping("/change-phone")
    @Operation(
            summary = "Change phone number",
            description = """
                    Use this endpoint to change phone number with the given information. \s
                    This endpoint is for changing, user have to provide password. \s
                    ### Authentication: Bearer token required. \s
                    ### Authorization: available authenticated users, user can change only his/her phone number.
                    """
    )
    @PreAuthorize(
            "(" +
                    "#changePhoneRequest.id == authentication.principal.id" +
            ") " +
            "and " +
            "@authServiceImpl.isPasswordMatched(" +
                    "#changePhoneRequest.id, " +
                    "#changePhoneRequest.password" +
            ")"
    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> changePhone(@RequestBody ChangePhoneRequest changePhoneRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.changePhone(changePhoneRequest));
    }

    // register low level worker
    @PostMapping("/register-worker")
    @Operation(
            summary = "Register a new worker",
            description = """
                    Use this endpoint to register a new worker to the system. \s
                    Once worker registered with this endpoint these roles will get automatically: \s
                        \
                        - ROLE_WORKER. \s
                    storeId in request body is required to register a worker if the authenticated user is admin. \s
                    storeId in request body is not required to register a worker if the authenticated user is manager. \s
                    ### Authentication: Bearer token required. \s
                    ### Authorization: \s
                        - available to admin to register worker to any store. \s
                        - available to manager to register worker to his/her store. \s
                    """
    )
//    @PreAuthorize(
//            "hasRole('ROLE_ADMIN') " +
//                    "or" +
//                    "(" +
//                        "hasRole('ROLE_MANAGER') " +
//                        "and " +
//                        "@workerServiceImpl.isManagerOfStore(" +
//                            "authentication.principal.id," +
//                            "#registerWorkerRequest.storeId" +
//                        ")" +
//                    ")"
//    )
    public ResponseEntity<ApiResponse<WorkerInfoResponse>> registerWorker(@RequestBody RegisterWorkerRequest registerWorkerRequest, @AuthenticationPrincipal Worker authenticatedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerWorker(registerWorkerRequest, authenticatedUser));
    }
}
