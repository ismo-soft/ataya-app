package com.ataya.beneficiary.service;

import com.ataya.beneficiary.dto.beneficiary.BeneficiaryDto;
import com.ataya.beneficiary.dto.beneficiary.CredentialRequest;
import com.ataya.beneficiary.dto.beneficiary.LoginResponse;
import com.ataya.beneficiary.util.ApiResponse;

public interface AuthService {
    ApiResponse<BeneficiaryDto> registerUser(CredentialRequest request);

    ApiResponse<LoginResponse> loginUser(CredentialRequest request);

    ApiResponse<BeneficiaryDto> updateUser(String id, BeneficiaryDto contributorDto);

    ApiResponse<BeneficiaryDto> getUserProfile(String id);
}
