package com.ataya.contributor.service.impl;

import com.ataya.contributor.dto.user.ContributorDto;
import com.ataya.contributor.dto.user.CredentialRequest;
import com.ataya.contributor.dto.user.LoginResponse;
import com.ataya.contributor.exception.custom.ValidationException;
import com.ataya.contributor.model.Contributor;
import com.ataya.contributor.repo.ContributorRepository;
import com.ataya.contributor.security.jwt.JwtService;
import com.ataya.contributor.service.AuthService;
import com.ataya.contributor.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ContributorRepository contributorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public ApiResponse<ContributorDto> registerUser(CredentialRequest request) {

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ValidationException(
                    "email",
                    "null",
                    "email is required"
            );
        }

        if (contributorRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException(
                    "email",
                    request.getEmail(),
                    "Email already exists"
            );
        }

        Contributor contributor = Contributor.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // In a real application, ensure to hash the password
                .enabled(true)
                .build();
        Contributor savedContributor = contributorRepository.save(contributor);
        ContributorDto contributorDto = ContributorDto.builder()
                .id(savedContributor.getId())
                .email(savedContributor.getEmail())
                .registrationDate(LocalDate.now())
                .enabled(savedContributor.isEnabled())
                .build();

        return ApiResponse.<ContributorDto>builder()
                .message("User registered successfully")
                .data(contributorDto)
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<LoginResponse> loginUser(CredentialRequest request) {
        Contributor contributor = contributorRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationException(
                        "email",
                        request.getEmail(),
                        "User not found"
                ));
        if (!passwordEncoder.matches(request.getPassword(), contributor.getPassword())) {
            throw new ValidationException(
                    "password",
                    request.getPassword(),
                    "Invalid password"
            );
        }
        String token = jwtService.generateToken(contributor);
        LoginResponse loginResponse = LoginResponse.builder()
                .contributor(ContributorDto.builder()
                        .id(contributor.getId())
                        .email(contributor.getEmail())
                        .enabled(contributor.isEnabled())
                        .build())
                .token(token)
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
    public ApiResponse<ContributorDto> updateUser(String id,ContributorDto contributorDto) {
        Contributor contributor = contributorRepository.findById(id)
                .orElseThrow(() -> new ValidationException(
                        "id",
                        contributorDto.getId(),
                        "User not found"
                ));

        if (contributorDto.getEmail() != null && !contributorDto.getEmail().isEmpty()) {
            if (contributorRepository.existsByEmail(contributorDto.getEmail())) {
                throw new ValidationException(
                        "email",
                        contributorDto.getEmail(),
                        "Email already exists"
                );
            }
            contributor.setEmail(contributorDto.getEmail());
        }

        if (contributorDto.getName() != null) {
            contributor.setName(contributorDto.getName());
        }

        if (contributorDto.getSurname() != null) {
            contributor.setSurname(contributorDto.getSurname());
        }

        if (contributorDto.getPhone() != null) {
            contributor.setPhone(contributorDto.getPhone());
        }

        if (contributorDto.getBio() != null) {
            contributor.setBio(contributorDto.getBio());
        }

        if (contributorDto.getProfilePhoto() != null) {
            contributor.setProfilePhoto(contributorDto.getProfilePhoto());
        }



        Contributor updatedContributor = contributorRepository.save(contributor);
        ContributorDto updatedContributorDto = ContributorDto.builder()
                .id(updatedContributor.getId())
                .name(updatedContributor.getName())
                .surname(updatedContributor.getSurname())
                .email(updatedContributor.getEmail())
                .phone(updatedContributor.getPhone())
                .bio(updatedContributor.getBio())
                .profilePhoto(updatedContributor.getProfilePhoto())
                .registrationDate(updatedContributor.getRegistrationDate())
                .totalContributions(updatedContributor.getTotalContributions())
                .enabled(updatedContributor.isEnabled())
                .build();

        return ApiResponse.<ContributorDto>builder()
                .message("User updated successfully")
                .data(updatedContributorDto)
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<ContributorDto> getUserProfile(String id) {
        if (id == null || id.isEmpty()) {
            throw new ValidationException(
                    "id",
                    "null",
                    "User ID is required"
            );
        }

        Contributor contributor = contributorRepository.findById(id)
                .orElseThrow(() -> new ValidationException(
                        "id",
                        id,
                        "User not found"
                ));

        ContributorDto contributorDto = ContributorDto.builder()
                .id(contributor.getId())
                .name(contributor.getName())
                .surname(contributor.getSurname())
                .email(contributor.getEmail())
                .phone(contributor.getPhone())
                .profilePhoto(contributor.getProfilePhoto())
                .bio(contributor.getBio())
                .registrationDate(contributor.getRegistrationDate())
                .totalContributions(contributor.getTotalContributions())
                .enabled(contributor.isEnabled())
                .build();

        return ApiResponse.<ContributorDto>builder()
                .message("User profile retrieved successfully")
                .data(contributorDto)
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
