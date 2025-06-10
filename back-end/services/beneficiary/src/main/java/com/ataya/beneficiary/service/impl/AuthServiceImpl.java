package com.ataya.beneficiary.service.impl;

import com.ataya.beneficiary.dto.beneficiary.BeneficiaryDto;
import com.ataya.beneficiary.dto.beneficiary.CredentialRequest;
import com.ataya.beneficiary.dto.beneficiary.LoginResponse;
import com.ataya.beneficiary.model.Beneficiary;
import com.ataya.beneficiary.repo.BeneficiaryRepository;
import com.ataya.beneficiary.security.jwt.JwtService;
import com.ataya.beneficiary.service.AuthService;
import com.ataya.beneficiary.util.ApiResponse;
import com.ataya.contributor.exception.custom.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<BeneficiaryDto> registerUser(CredentialRequest request) {
        if (request.getIdentityNumber() == null || request.getIdentityNumber().isEmpty()) {
            throw new ValidationException(
                    "identity number",
                    "null",
                    "email is required"
            );
        }
        if (beneficiaryRepository.existsByIdentityNumber(request.getIdentityNumber())) {
            throw new ValidationException(
                    "identity number",
                    request.getIdentityNumber(),
                    "Identity number already exists"
            );
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .identityNumber(request.getIdentityNumber())
                .password(passwordEncoder.encode(request.getPassword())) // Ensure to hash the password
                .enabled(true)
                .build();
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        BeneficiaryDto beneficiaryDto = BeneficiaryDto.builder()
                .id(savedBeneficiary.getId())
                .build();

        return ApiResponse.<BeneficiaryDto>builder()
                .message("User registered successfully")
                .data(beneficiaryDto)
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<LoginResponse> loginUser(CredentialRequest request) {
        Beneficiary beneficiary = beneficiaryRepository.findByIdentityNumber(request.getIdentityNumber())
                .orElseThrow(() -> new ValidationException(
                        "identity number",
                        request.getIdentityNumber(),
                        "User not found"
                ));

        if (!passwordEncoder.matches(request.getPassword(), beneficiary.getPassword())) {
            throw new ValidationException(
                    "password",
                    "invalid",
                    "Invalid password"
            );
        }

        String token = jwtService.generateToken(beneficiary);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(token)
                .beneficiaryDto(
                        BeneficiaryDto.builder()
                                .id(beneficiary.getId())
                                .name(beneficiary.getName())
                                .surname(beneficiary.getSurname())
                                .email(beneficiary.getEmail())
                                .phoneNumber(beneficiary.getPhoneNumber())
                                .build()
                )
                .build();

        return ApiResponse.<LoginResponse>builder()
                .message("Login successful")
                .data(loginResponse)
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<BeneficiaryDto> updateUser(String id, BeneficiaryDto contributorDto) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ValidationException(
                        "id",
                        id,
                        "User not found"
                ));

        if (contributorDto.getName() != null) {
            beneficiary.setName(contributorDto.getName());
        }
        if (contributorDto.getSurname() != null) {
            beneficiary.setSurname(contributorDto.getSurname());
        }
        if (contributorDto.getEmail() != null) {
            beneficiary.setEmail(contributorDto.getEmail());
        }
        if (contributorDto.getPhoneNumber() != null) {
            beneficiary.setPhoneNumber(contributorDto.getPhoneNumber());
        }

        Beneficiary updatedBeneficiary = beneficiaryRepository.save(beneficiary);
        BeneficiaryDto updatedBeneficiaryDto = BeneficiaryDto.builder()
                .id(updatedBeneficiary.getId())
                .name(updatedBeneficiary.getName())
                .surname(updatedBeneficiary.getSurname())
                .email(updatedBeneficiary.getEmail())
                .phoneNumber(updatedBeneficiary.getPhoneNumber())
                .build();

        return ApiResponse.<BeneficiaryDto>builder()
                .message("User updated successfully")
                .data(updatedBeneficiaryDto)
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<BeneficiaryDto> getUserProfile(String id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ValidationException(
                        "id",
                        id,
                        "User not found"
                ));

        BeneficiaryDto beneficiaryDto = BeneficiaryDto.builder()
                .id(beneficiary.getId())
                .name(beneficiary.getName())
                .surname(beneficiary.getSurname())
                .email(beneficiary.getEmail())
                .phoneNumber(beneficiary.getPhoneNumber())
                .build();

        return ApiResponse.<BeneficiaryDto>builder()
                .message("User profile retrieved successfully")
                .data(beneficiaryDto)
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
