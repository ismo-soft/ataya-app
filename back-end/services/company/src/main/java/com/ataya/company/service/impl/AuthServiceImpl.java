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
                // TODO: set enabled to false when email verification is implemented
                .enabled(true)
                .build();

        // save the worker
        workerRepository.save(worker);

        // TODO: send an email verification link to the worker

        // return the response
         return ApiResponse.builder()
                .message("Worker registered successfully")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .build();

    }

    @Override
    public ApiResponse login(LoginRequest loginRequest) {
        // load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
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
                                .phone(((Worker) userDetails).getPhone())
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
                                .phone(worker.getPhone())
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
                                .phone(worker.getPhone())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }

    // TODO: implement invalidating tokens when the username is changed
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
                                .phone(worker.getPhone())
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
        if (workerRepository.existsByPhone(changePhoneRequest.getPhone())) {
            throw new ValidationException(
                    "phone",
                    changePhoneRequest.getPhone(),
                    "Phone number is already taken"
            );
        }
        // change the phone number
        worker.setPhone(changePhoneRequest.getPhone());
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
                                .phone(worker.getPhone())
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
                // TODO : will be set to false when email verification is implemented
                .enabled(true)
                .build();

        // save
        workerRepository.save(worker);

        // TODO: implement email verification
        // send email verification link

        // return response
        return ApiResponse.builder()
                .message("Worker registered successfully")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(
                        WorkerInfoResponse.builder()
                                .id(worker.getId())
                                .username(worker.getUsername())
                                .email(worker.getEmail())
                                .phone(worker.getPhone())
                                .companyId(worker.getCompanyId())
                                .storeId(worker.getStoreId())
                                .build()
                )
                .build();
    }
}
