package com.ataya.company.service;

import com.ataya.company.dto.worker.request.*;
import com.ataya.company.util.ApiResponse;

public interface AuthService {
    ApiResponse register(RegisterRequest request);

    ApiResponse login(LoginRequest loginRequest);

    ApiResponse changePassword(ChangePasswordRequest changePasswordRequest);

    ApiResponse changeEmail(ChangeEmailRequest changeEmailRequest);

    ApiResponse changeUsername(ChangeUsernameRequest changeUsernameRequest);

    ApiResponse changePhone(ChangePhoneRequest changePhoneRequest);

    boolean isPasswordMatched(String id, String password);

    ApiResponse registerWorker(RegisterWorkerRequest registerWorkerRequest);
}
