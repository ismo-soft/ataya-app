package com.ataya.company.service;

import com.ataya.company.dto.worker.request.*;
import com.ataya.company.dto.worker.response.WorkerInfoResponse;
import com.ataya.company.model.Worker;
import com.ataya.company.util.ApiResponse;

public interface AuthService {
    ApiResponse<WorkerInfoResponse> register(RegisterRequest request);

    ApiResponse<WorkerInfoResponse> login(LoginRequest loginRequest);

    ApiResponse<WorkerInfoResponse> changePassword(ChangePasswordRequest changePasswordRequest);

    ApiResponse<WorkerInfoResponse> changeEmail(ChangeEmailRequest changeEmailRequest);

    ApiResponse<WorkerInfoResponse> changeUsername(ChangeUsernameRequest changeUsernameRequest);

    ApiResponse<WorkerInfoResponse> changePhone(ChangePhoneRequest changePhoneRequest);

    boolean isPasswordMatched(String id, String password);

    ApiResponse<WorkerInfoResponse> verifyEmail(String token, String email, String id, String username);

    ApiResponse<WorkerInfoResponse> forgotPassword(String email);

    ApiResponse<WorkerInfoResponse> resetPassword(String token, String id, String username, String email, ResetPasswordRequest password);

    ApiResponse<WorkerInfoResponse> registerWorker(RegisterWorkerRequest registerWorkerRequest, Worker authenticatedUser);
}
