package com.ataya.company.service.impl;

import com.ataya.company.dto.worker.request.*;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.enums.Role;
import com.ataya.company.exception.custom.InvalidOperationException;
import com.ataya.company.exception.custom.ResourceNotFoundException;
import com.ataya.company.exception.custom.ValidationException;
import com.ataya.company.model.Worker;
import com.ataya.company.repo.WorkerRepository;
import com.ataya.company.security.jwt.JwtService;
import com.ataya.company.security.service.UserDetailsServiceImpl;
import com.ataya.company.service.AuthService;
import com.ataya.company.service.StoreService;
import com.ataya.company.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final WorkerRepository workerRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtService jwtService;

    private final StoreService storeService;

    public AuthServiceImpl(WorkerRepository workerRepository, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService, JwtService jwtService, StoreService storeService) {
        this.workerRepository = workerRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.storeService = storeService;
    }


    @Override
    public ApiResponse<WorkerInfoResponse> register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new ValidationException(
                    "username",
                    request.getUsername(),
                    "Username is required"
            );
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ValidationException(
                    "email",
                    request.getEmail(),
                    "Email is required"
            );
        }
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
        ApiResponse<WorkerInfoResponse> response = new ApiResponse<>();
        response.setMessage("Worker registered successfully");
        response.setStatus(HttpStatus.CREATED.getReasonPhrase());
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setTimestamp(LocalDateTime.now());
        response.setData(
                WorkerInfoResponse.builder()
                        .id(worker.getId())
                        .username(worker.getUsername())
                        .email(worker.getEmail())
                        .phone(worker.getPhoneNumber())
                        .token(token)
                        .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
                        .companyId(worker.getCompanyId())
                        .storeId(worker.getStoreId())
                        .build()
        );
        return response;
    }

    @Override
    public ApiResponse<WorkerInfoResponse> login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        Worker worker = workerRepository.findByEmailOrUsername(loginRequest.getEmail(),loginRequest.getUsername()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "email",
                        loginRequest.getEmail(),
                        "Worker not found"
                )
        );
        // load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // check if the password is correct
        if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            throw new ValidationException(
                    "password",
                    loginRequest.getPassword(),
                    "Password is incorrect"
            );
        }
        if (!userDetails.isEnabled()) {
            throw new ValidationException(
                    "email",
                    loginRequest.getEmail(),
                    "Email is not verified"
            );
        }
        // generate a token
        String token = jwtService.generateToken(userDetails);
        // return the token
        return ApiResponse.<WorkerInfoResponse>builder()
                .message("Login successful")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(userDetails.getUsername())
                                .username(userDetails.getUsername())
                                .email(userDetails.getUsername())
                                .phone(userDetails.getUsername())
                                .token(token)
                                .roles(userDetails.getAuthorities().stream().map(Object::toString).toList())
                                .companyId(worker.getCompanyId())
                                .storeId(userDetails.getUsername())
                                .build()
                ).build();
    }

    @Override
    public ApiResponse<WorkerInfoResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
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
        return ApiResponse.<WorkerInfoResponse>builder()
                .message("Password changed successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse<WorkerInfoResponse> changeEmail(ChangeEmailRequest changeEmailRequest) {
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
        // set email verification token
        String token = UUID.randomUUID().toString();
        worker.setEmailVerificationToken(token);
        // return the response
        return ApiResponse.<WorkerInfoResponse>builder()
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
                                .token(token)
                                .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse<WorkerInfoResponse> changeUsername(ChangeUsernameRequest changeUsernameRequest) {
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
        return ApiResponse.<WorkerInfoResponse>builder()
                .message("Username changed successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse<WorkerInfoResponse> changePhone(ChangePhoneRequest changePhoneRequest) {
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
        return ApiResponse.<WorkerInfoResponse>builder()
                .message("Phone number changed successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
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
    public ApiResponse<WorkerInfoResponse> verifyEmail(String token, String email, String id, String username) {
        Worker worker = workerRepository.findByEmailVerificationToken(token).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Worker",
                        "emailVerificationToken",
                        token,
                        "Worker not found"
                )
        );
        // verify the email
        worker.setEnabled(true);
        worker.setEmailVerificationToken(null);
        // save
        workerRepository.save(worker);
        // return response
        return ApiResponse.<WorkerInfoResponse>builder()
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
                                .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
                                .build()
                )
                .build();
    }

    private Worker getWorker(String email, String id, String username) {
        if (email != null) {
            return  workerRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "email",
                            email,
                            "Worker not found"
                    )
            );
        } else if (id != null) {
            return workerRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "id",
                            id,
                            "Worker not found"
                    )
            );
        } else if (username != null) {
            return workerRepository.findByUsername(username).orElseThrow(
                    () -> new ResourceNotFoundException(
                            "Worker",
                            "username",
                            username,
                            "Worker not found"
                    )
            );
        } else {
            throw new ValidationException(
                    "email, id, username",
                    null,
                    "One of them is required"
            );
        }
    }

    @Override
    public ApiResponse<WorkerInfoResponse> forgotPassword(String email) {
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
        return ApiResponse.<WorkerInfoResponse>builder()
                .message("Password reset token created successfully")
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
                                .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
                                .token(token)
                                .build()

                )
                .build();
    }

    @Override
    public ApiResponse<WorkerInfoResponse> resetPassword(String token, String id, String username, String email, ResetPasswordRequest password) {
        // check if the password is valid
        if (password.getConfirmPassword() == null || !password.getNewPassword().equals(password.getConfirmPassword())) {
            throw new ValidationException(
                    "confirmPassword",
                    password.getConfirmPassword(),
                    "Passwords do not match"
            );
        }
        // check if worker exists
        Worker worker = getWorker(email, id, username);
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
        return ApiResponse.<WorkerInfoResponse>builder()
                .message("Password reset successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
                                .phone(worker.getPhoneNumber())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public ApiResponse<WorkerInfoResponse> registerWorker(RegisterWorkerRequest registerWorkerRequest, Worker authenticatedUser) {
        // if user is admin and company id is null then throw exception and tell user to create company first
        // if user is admin and company id is not null but store id in request is null then throw exception and tell user to provide store id
        if (authenticatedUser.getRoles().contains(Role.ROLE_ADMIN)) {
            if (authenticatedUser.getCompanyId() == null) {
                throw new ValidationException(
                        "companyId",
                        "null",
                        "No company found, create a company first"
                );
            }
            if (registerWorkerRequest.getStoreId() == null) {
                throw new ValidationException(
                        "storeId",
                        "null",
                        "Provide store id, Create a store if you have no store"
                );
            }
        }
        // if user is manager and company id is null then throw exception and tell user this is illegal operation
        // if user is manager and company id is not null but user store id is null then throw exception and tell user this is illegal operation
        if (authenticatedUser.getRoles().contains(Role.ROLE_MANAGER) && !authenticatedUser.getRoles().contains(Role.ROLE_ADMIN)) {
            if (authenticatedUser.getCompanyId() == null) {
                throw new InvalidOperationException(
                        "Creating worker",
                        "You are not allowed at any company"
                );
            }
            if (authenticatedUser.getStoreId() == null) {
                throw new InvalidOperationException(
                        "Creating worker",
                        "You are not allowed at any store"
                );
            }
        }
        // check store exists and is in the same company
        if (registerWorkerRequest.getStoreId() != null) {
            if (!storeService.isStoreOfCompany(registerWorkerRequest.getStoreId(), authenticatedUser.getCompanyId())) {
                throw new ResourceNotFoundException(
                        "Store",
                        "id",
                        registerWorkerRequest.getStoreId(),
                        "Store not found or not in the same company"
                );
            }
        }
        // check if the username is already taken
        if (workerRepository.existsByUsername(registerWorkerRequest.getUsername())) {
            throw new ValidationException(
                    "username",
                    registerWorkerRequest.getUsername(),
                    "Username is already taken"
            );
        }
        // check if the email is already taken
        if (workerRepository.existsByEmail(registerWorkerRequest.getEmail())) {
            throw new ValidationException(
                    "email",
                    registerWorkerRequest.getEmail(),
                    "Email is already taken"
            );
        }
        // create a new worker
        Worker worker = Worker.builder()
                .username(registerWorkerRequest.getUsername())
                .email(registerWorkerRequest.getEmail())
                .password(passwordEncoder.encode(registerWorkerRequest.getPassword()))
                .roles(
                        List.of(
                                Role.ROLE_WORKER
                        )
                )
                .enabled(false)
                .companyId(authenticatedUser.getCompanyId())
                .storeId(registerWorkerRequest.getStoreId() != null ? registerWorkerRequest.getStoreId() : authenticatedUser.getStoreId())
                .build();
        // create verification token
        String token = UUID.randomUUID().toString();
        worker.setEmailVerificationToken(token);
        // save the worker
        workerRepository.save(worker);
        // return the response
        return ApiResponse.<WorkerInfoResponse>builder()
                .message("Worker registered successfully")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .phone(worker.getPhoneNumber())
                                .roles(worker.getRoles().stream().map(Role::getRoleAsString).toList())
                                .token(token)
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    @Override
    public String getRoles(Worker authenticatedPrincipal) {
        // get roles
        return authenticatedPrincipal.getRoles().toString();
    }

    @Override
    public Boolean isTokenExpired(String token) {
        jwtService.isTokenExpired(token);
    }

}
