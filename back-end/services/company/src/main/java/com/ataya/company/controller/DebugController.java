package com.ataya.company.controller;

import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.model.Worker;
import com.ataya.company.service.AuthService;
import com.ataya.company.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company/debug")
public class DebugController {

    private final AuthService authService;
    private final CompanyService companyService;

    public DebugController(AuthService authService, CompanyService companyService) {
        this.authService = authService;
        this.companyService = companyService;
    }


    // my roles
    @GetMapping("/my-roles")
    @Operation(
            summary = "Get user roles",
            description = """
                    This endpoint is used to get the roles of the authenticated user. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role DEBUG can get my roles. \s
                    """
    )
    public String getMyRoles(@AuthenticationPrincipal Worker authenticatedPrincipal) {
        return authService.getRoles(authenticatedPrincipal);
    }

    // my company
    @GetMapping("/my-company")
    @Operation(
            summary = "Get my company",
            description = """
                    This endpoint is used to get the company of the authenticated user. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: user with role DEBUG can get my company. \s
                    """
    )
    public CompanyInfoResponse getMyCompany(@AuthenticationPrincipal Worker authenticatedPrincipal) {
        System.out.println("DEBUG: getMyCompany");
        System.out.println("DEBUG: authenticatedPrincipal: " + authenticatedPrincipal);
        System.out.println("DEBUG: authenticatedPrincipal.getCompanyId(): " + authenticatedPrincipal.getCompanyId());
        System.out.println("DEBUG: authenticationPrincipal.Username " + authenticatedPrincipal.getUsername());
        System.out.println("DEBUG: authenticationPrincipal.getRoles() " + authenticatedPrincipal.getRoles());
        System.out.println("DEBUG: authenticationPrincipal.getId() " + authenticatedPrincipal.getId());
        return companyService.getCompany(authenticatedPrincipal.getCompanyId());
    }


}
