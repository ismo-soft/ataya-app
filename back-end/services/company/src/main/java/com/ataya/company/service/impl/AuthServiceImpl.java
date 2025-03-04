package com.ataya.company.service.impl;

import com.ataya.company.dto.worker.request.*;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.enums.Role;
import com.ataya.company.exception.Custom.ResourceNotFoundException;
import com.ataya.company.exception.Custom.ValidationException;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.WorkerRepository;
import com.ataya.company.security.jwt.JwtService;
import com.ataya.company.service.AuthService;
import com.ataya.company.service.StoreService;
import com.ataya.company.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private StoreService storeService;



    @Override
    public ApiResponse register(RegisterRequest request) {
        // check if the username is already taken
        if (workerRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException(
                    "username",
                    request.getUsername(),
                    "Username is already taken"
            );
        }
        // check if the email is already taken
        if (workerRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException(
                    "email",
                    request.getEmail(),
                    "Email is already taken"
            );
        }
        // create a new worker
        Worker worker = Worker.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(
                        List.of(
                                Role.ROLE_SUPER_ADMIN,
                                Role.ROLE_ADMIN,
                                Role.ROLE_MANAGER,
                                Role.ROLE_WORKER
                        )
                )
                .enabled(false)
                .build();

        // create verification token
        String token = UUID.randomUUID().toString();
        worker.setEmailVerificationToken(token);


        // save the worker
        workerRepository.save(worker);


        // return the response
         return ApiResponse.builder()
                .message("Worker registered successfully")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                 .data(
                         Map.of(
                                    "verificationToken", token,
                                    "worker", WorkerInfoResponse.builder()
                                            .id(worker.getId())
                                            .username(worker.getUsername())
                                            .email(worker.getEmail())
                                            .phone(worker.getPhoneNumber())
                                            .companyId(worker.getCompanyId())
                                            .storeId(worker.getStoreId())
                                            .build()
                                 )
                 )
                .build();

    }

    @Override
    public ApiResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        if (loginRequest.getEmail() != null && username == null) {
            Worker worker = workerRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "email",
                            loginRequest.getEmail(),
                            "Worker not found"
                    )
            );
            username = worker.getUsername();
        }
        // load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // generate a token
        String token = jwtService.generateToken(userDetails);
        // return the token
        return ApiResponse.builder()
                .message("Login successful")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .token(token)
                                .id(((Worker) userDetails).getId())
                                .username(((Worker) userDetails).getUsername())
                                .email(((Worker) userDetails).getEmail())
                                .phone(((Worker) userDetails).getPhoneNumber())
                                .companyId(((Worker) userDetails).getCompanyId())
                                .storeId(((Worker) userDetails).getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        // check if the worker exists
        Worker worker = workerRepository.findById(changePasswordRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "id",
                        changePasswordRequest.getId(),
                        "Worker not found"
                )
        );
        // check if the old password is correct
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), worker.getPassword())) {
            throw new ValidationException(
                    "oldPassword",
                    changePasswordRequest.getOldPassword(),
                    "Old password is incorrect"
            );
        }
        // change the password
        worker.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        // save the worker
        workerRepository.save(worker);
        // return the response
        return ApiResponse.builder()
                .message("Password changed successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse changeEmail(ChangeEmailRequest changeEmailRequest) {
        // check if the worker exists
        Worker worker = workerRepository.findById(changeEmailRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "id",
                        changeEmailRequest.getId(),
                        "Worker not found"
                )
        );
        // check if the email is already taken
        if (workerRepository.existsByEmail(changeEmailRequest.getEmail())) {
            throw new ValidationException(
                    "email",
                    changeEmailRequest.getEmail(),
                    "Email is already taken"
            );
        }
        // change the email
        worker.setEmail(changeEmailRequest.getEmail());
        // save the worker
        workerRepository.save(worker);
        // return the response
        return ApiResponse.builder()
                .message("Email changed successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse changeUsername(ChangeUsernameRequest changeUsernameRequest) {
        // check if the worker exists
        Worker worker = workerRepository.findById(changeUsernameRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "id",
                        changeUsernameRequest.getId(),
                        "Worker not found"
                )
        );
        // check if the username is already taken
        if (workerRepository.existsByUsername(changeUsernameRequest.getNewUsername())) {
            throw new ValidationException(
                    "username",
                    changeUsernameRequest.getNewUsername(),
                    "Username is already taken"
            );
        }

        // change the username
        worker.setUsername(changeUsernameRequest.getNewUsername());

        // save the worker
        workerRepository.save(worker);

        // logout the worker

        // return the response
        return ApiResponse.builder()
                .message("Username changed successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse changePhone(ChangePhoneRequest changePhoneRequest) {
        // check if the worker exists
        Worker worker = workerRepository.findById(changePhoneRequest.getId()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "id",
                        changePhoneRequest.getId(),
                        "Worker not found"
                )
        );
        // check if the phone number is already taken
        if (workerRepository.existsByPhoneNumber(changePhoneRequest.getPhoneNumber())) {
            throw new ValidationException(
                    "phone",
                    changePhoneRequest.getPhoneNumber(),
                    "Phone number is already taken"
            );
        }
        // change the phone number
        worker.setPhoneNumber(changePhoneRequest.getPhoneNumber());
        // save the worker
        workerRepository.save(worker);
        // return the response
        return ApiResponse.builder()
                .message("Phone number changed successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public boolean isPasswordMatched(String id, String password) {
        // check if the worker exists
        Worker worker = workerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "id",
                        id,
                        "Worker not found"
                )
        );
        // check if the password is correct
        return passwordEncoder.matches(password, worker.getPassword());
    }

    @Override
    public ApiResponse registerWorker(RegisterWorkerRequest registerWorkerRequest) {
        // check if username is already taken
        if (workerRepository.existsByUsername(registerWorkerRequest.getUsername())) {
            throw new ValidationException(
                    "username",
                    registerWorkerRequest.getUsername(),
                    "Username is already taken"
            );
        }
        // check if email is already taken
        if (workerRepository.existsByEmail(registerWorkerRequest.getEmail())) {
            throw new ValidationException(
                    "email",
                    registerWorkerRequest.getEmail(),
                    "Email is already taken"
            );
        }
        // check if store exists
        if (!storeService.existsById(registerWorkerRequest.getStoreId())) {
            throw new ResourceNotFoundException(
                    "Store",
                    "id",
                    registerWorkerRequest.getStoreId(),
                    "Store not found"
            );
        }
        // create worker
        Worker worker = Worker.builder()
                .username(registerWorkerRequest.getUsername())
                .email(registerWorkerRequest.getEmail())
                .password(passwordEncoder.encode(registerWorkerRequest.getPassword()))
                .storeId(registerWorkerRequest.getStoreId())
                .roles(
                        List.of(
                                Role.ROLE_WORKER
                        )
                )
                .companyId(storeService.getCompanyId(registerWorkerRequest.getStoreId()))
                .managerId(storeService.getManagerId(registerWorkerRequest.getStoreId()))
                .enabled(false)
                .build();

        // create verification token
        String token = UUID.randomUUID().toString();
        worker.setEmailVerificationToken(token);
        // save
        workerRepository.save(worker);

        // return response
        return ApiResponse.builder()
                .message("Worker registered successfully")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(
                        Map.of(
                                "verificationToken", token,
                                "worker", WorkerInfoResponse.builder()
                                        .id(worker.getId())
                                        .username(worker.getUsername())
                                        .email(worker.getEmail())
                                        .phone(worker.getPhoneNumber())
                                        .companyId(worker.getCompanyId())
                                        .storeId(worker.getStoreId())
                                        .build()
                        )
                )
                .build();
    }

    @Override
    public ApiResponse verifyEmail(String token, String email, String id, String username) {
        // check if worker exists
        Worker worker = null;
        if (email != null) {
            worker = workerRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "email",
                            email,
                            "Worker not found"
                    )
            );
        } else if (id != null) {
            worker = workerRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "id",
                            id,
                            "Worker not found"
                    )
            );
        } else if (username != null) {
            worker = workerRepository.findByUsername(username).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "username",
                            username,
                            "Worker not found"
                    )
            );
        }else {
            throw new ValidationException(
                    "email, id, username",
                    null,
                    "One of them is required"
            );
        }
        // check if the token is correct
        if (!worker.getEmailVerificationToken().equals(token)) {
            throw new ValidationException(
                    "token",
                    token,
                    "Token is incorrect"
            );
        }
        // verify the email
        worker.setEnabled(true);
        worker.setEmailVerificationToken(null);
        // save
        workerRepository.save(worker);
        // return response
        return ApiResponse.builder()
                .message("Email verified successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse forgotPassword(String email) {
        Worker worker = workerRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "email",
                        email,
                        "Worker not found"
                )
        );
        if (!worker.isEnabled() || worker.getEmailVerificationToken() != null){
            throw new ValidationException(
                    "email",
                    email,
                    "Email is not verified"
            );
        }
        // create token
        String token = UUID.randomUUID().toString();
        worker.setPasswordResetToken(token);
        // save token
        workerRepository.save(worker);
        // create response with token
        return ApiResponse.builder()
                .message("Password reset token created successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        Map.of(
                                "resetToken", token
                        )
                )
                .build();
    }

    @Override
    public ApiResponse resetPassword(String token, String id, String username, String email, ResetPasswordRequest password) {
        // check if the password is valid
        if (password.getConfirmPassword() == null || !password.getNewPassword().equals(password.getConfirmPassword())) {
            throw new ValidationException(
                    "confirmPassword",
                    password.getConfirmPassword(),
                    "Passwords do not match"
            );
        }
        // check if worker exists
        Worker worker = null;
        if (email != null) {
            worker = workerRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "email",
                            email,
                            "Worker not found"
                    )
            );
        } else if (id != null) {
            worker = workerRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "id",
                            id,
                            "Worker not found"
                    )
            );
        } else if (username != null) {
            worker = workerRepository.findByUsername(username).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "username",
                            username,
                            "Worker not found"
                    )
            );
        }else {
            throw new ValidationException(
                    "email, id, username",
                    null,
                    "One of them is required"
            );
        }
        // check if the token is correct
        if (!worker.getPasswordResetToken().equals(token)) {
            throw new ValidationException(
                    "token",
                    token,
                    "Token is incorrect"
            );
        }
        // reset the password
        worker.setPassword(passwordEncoder.encode(password.getNewPassword()));
        worker.setPasswordResetToken(null);
        // save
        workerRepository.save(worker);
        // return response
        return ApiResponse.builder()
                .message("Password reset successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

}
