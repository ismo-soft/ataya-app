package com.ataya.company.controller;


import com.ataya.company.dto.company.CreateCompanyRequest;
import com.ataya.company.dto.company.UpdateCompanyRequest;
import com.ataya.company.model.Worker;
import com.ataya.company.service.CompanyService;
import com.ataya.company.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/company")
@RestController
public class CompanyController {
    /**
     * 1. Create a new company
     * 2. Get company information
     * 3. Update company information
     * 4. view all stores of a company
     * 5. view all workers of a company
     * 6. view all managers of a company
     *
     */

    @Autowired
    private CompanyService companyService;

    // create a new company
    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(
            summary = "Create a new company",
            description = "Create a new company with the given information"
    )
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CreateCompanyRequest createCompanyRequest,@AuthenticationPrincipal Worker authenticatedPrincipal) {
        return ResponseEntity.ok(companyService.createCompany(createCompanyRequest,authenticatedPrincipal.getId()));
    }

    // get company information
    @GetMapping("/profile/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
            "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "Get company information",
            description = "Get company information with the given company id"
    )
    public ResponseEntity<ApiResponse> getCompanyDetails(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.getCompanyDetails(companyId));
    }

    // update company information
    @PutMapping("/profile/update/{companyId}")
    @PreAuthorize(
            "hasRole('SUPER_ADMIN')"
    )
    @Operation(
            summary = "Update company information",
            description = "Update company information with the given company id"
    )
    public ResponseEntity<ApiResponse> updateCompany(@PathVariable String companyId, @RequestBody UpdateCompanyRequest updateCompanyRequest) {
        return ResponseEntity.ok(companyService.updateCompany(companyId, updateCompanyRequest));
    }
}
