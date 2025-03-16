package com.ataya.company.controller;

import com.ataya.company.dto.company.CompanyInfoResponse;
import com.ataya.company.dto.store.response.StoreInfoResponse;
import com.ataya.company.model.Worker;
import com.ataya.company.service.AuthService;
import com.ataya.company.service.CompanyService;
import com.ataya.company.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/company/debug")
public class DebugController {

    private final AuthService authService;
    private final CompanyService companyService;
    private final StoreService storeService;

    public DebugController(AuthService authService, CompanyService companyService, StoreService storeService) {
        this.authService = authService;
        this.companyService = companyService;
        this.storeService = storeService;
    }


    // my roles
    @GetMapping("/my-roles")
    @Operation(
            summary = "Get user roles",
            description = """
                    This endpoint is used to get the roles of the authenticated user. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: any authenticated user can get my roles. \s
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
                    ### Authorizations: any authenticated user can get my company. \s
                    """
    )
    public CompanyInfoResponse getMyCompany(@AuthenticationPrincipal Worker authenticatedPrincipal) {
        return companyService.getCompany(authenticatedPrincipal.getCompanyId());
    }

    // my stores
    @GetMapping("/my-stores")
    @Operation(
            summary = "Get my stores",
            description = """
                    This endpoint is used to get the stores of the authenticated user. \s
                    ### Authentication: bearer token is required. \s
                    ### Authorizations: any authenticated user can get my stores. \s
                    """
    )
    public List<StoreInfoResponse> getMyStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal Worker authenticatedPrincipal) {
        return storeService.getStores(null, null, null, null, page, size, authenticatedPrincipal.getCompanyId()).getData();
    }



}
