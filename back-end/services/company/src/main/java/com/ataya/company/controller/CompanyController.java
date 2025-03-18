package com.ataya.company.controller;

import com.ataya.company.dto.company.CompanyDetailsResponse;
import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.company.UpdateCompanyRequest;
import com.ataya.company.model.Worker;
import com.ataya.company.service.CompanyService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // create company
    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(
            summary = "Create a new company",
            description = """
                    Create a new company with the given company information. \s
                    Worker with companyId == null can create company. \s
                    Be sure to have unique registration number. \s
                    Once company is created, the worker will be assigned as the CEO of the company. \s
                    Once company is created, owner or admin can fill the company information(company profile). \s
                    ### Authentication: bearer token is required \s
                    ### Authorizations: user with role SUPER_ADMIN can create company
                    """
    )
    public ResponseEntity<ApiResponse<CompanyInfoResponse>> createCompany(@Valid @RequestBody CreateCompanyRequest createCompanyRequest, @AuthenticationPrincipal Worker authenticatedPrincipal) {
        return ResponseEntity.status(201).body(companyService.createCompany(createCompanyRequest, authenticatedPrincipal.getId()));
    }

    // get company info
    @GetMapping("/profile/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
                    "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "Get company information",
            description = """
                    Get company information with the given company id. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role SUPER_ADMIN or ADMIN can get company information. \s
                    """
    )
    public ResponseEntity<ApiResponse<CompanyInfoResponse>> getCompanyInfo(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.getCompanyInfo(companyId));
    }

    // update company info
    @PutMapping("/profile/{companyId}")
    @PreAuthorize(
            "hasRole('SUPER_ADMIN')"
    )
    @Operation(
            summary = "Update company information",
            description = """
                    Update company information with the given company id. \s
                    Be sure to have valid social media platforms and legal entity type. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role SUPER_ADMIN can update company information
                    """
    )
    public ResponseEntity<ApiResponse<CompanyInfoResponse>> updateCompany(@PathVariable String companyId, @RequestPart UpdateCompanyRequest updateCompanyRequest, @RequestPart(required = false) MultipartFile logo, @RequestPart(required = false) MultipartFile coverPhoto, @RequestPart(required = false) MultipartFile profilePhoto) {
        return ResponseEntity.ok(companyService.updateCompany(companyId, updateCompanyRequest, logo, coverPhoto, profilePhoto));
    }

    // view company details
    @GetMapping("/details/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
                    "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "View company details",
            description = """
                    Get company details with the given company id. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role SUPER_ADMIN or ADMIN can view company details. \s
                    """
    )
    public ResponseEntity<ApiResponse<CompanyDetailsResponse>> getCompanyDetails(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetails(companyId));
    }

    // set company address
    @PutMapping("/address/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
                    "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "Set company addressId",
            description = """
                    Set company addressId with the given company id. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role SUPER_ADMIN or ADMIN can set company addressId. \s
                    """
    )
    public ResponseEntity<ApiResponse<CompanyInfoResponse>> setCompanyAddressId(@PathVariable String companyId, @RequestParam String addressId) {
        return ResponseEntity.ok(companyService.setCompanyAddressId(companyId, addressId));
    }

}
