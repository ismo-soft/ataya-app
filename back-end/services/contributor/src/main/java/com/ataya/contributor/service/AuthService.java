package com.ataya.contributor.service;

import com.ataya.contributor.dto.user.ContributorDto;
import com.ataya.contributor.dto.user.CredentialRequest;
import com.ataya.contributor.dto.user.LoginResponse;
import com.ataya.contributor.util.ApiResponse;

public interface AuthService {
    
    ApiResponse<ContributorDto> registerUser(CredentialRequest request);

    ApiResponse<LoginResponse> loginUser(CredentialRequest request);

    ApiResponse<ContributorDto> updateUser(String id, ContributorDto contributorDto);

    ApiResponse<ContributorDto> getUserProfile(String id);
}
