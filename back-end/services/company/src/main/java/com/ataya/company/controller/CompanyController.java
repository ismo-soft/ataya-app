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
    @PutMapping("/profile/{companyId}")
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

    // view all stores of a company
    @GetMapping("/stores/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
            "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "View all stores of a company",
            description = "View all stores of a company with the given company id"
    )
    public ResponseEntity<ApiResponse> viewAllStores(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.viewAllStores(companyId));
    }

    // view all workers of a company
    @GetMapping("/workers/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
            "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "View all workers of a company",
            description = "View all workers of a company with the given company id"
    )
    public ResponseEntity<ApiResponse> viewAllWorkers(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.viewAllWorkersOfCompany(companyId));
    }

    // view all managers of a company
    @GetMapping("/managers/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
            "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "View all managers of a company",
            description = "View all managers of a company with the given company id"
    )
    public ResponseEntity<ApiResponse> viewAllManagers(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.viewAllManagersOfCompany(companyId));
    }

    // view company details
    @GetMapping("/details/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
            "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "View company details",
            description = "View company details with the given company id"
    )
    public ResponseEntity<ApiResponse> viewCompanyDetails(@PathVariable String companyId) {
        return ResponseEntity.ok(companyService.viewCompanyDetails(companyId));
    }

    // set company addressId
    @PutMapping("/address/{companyId}")
    @PreAuthorize(
            "(hasRole('SUPER_ADMIN') or hasRole('ADMIN')) and" +
            "#companyId == authentication.principal.companyId"
    )
    @Operation(
            summary = "Set company addressId",
            description = "Set company addressId with the given company id"
    )
    public ResponseEntity<ApiResponse> setCompanyAddressId(@PathVariable String companyId, @RequestParam String addressId) {
        return ResponseEntity.ok(companyService.setCompanyAddressId(companyId, addressId));
    }
}
