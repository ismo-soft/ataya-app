package com.ataya.company.controller;

import com.ataya.company.dto.bankAccount.BankAccountInfoResponse;
import com.ataya.company.dto.bankAccount.SetBankAccountRequest;
import com.ataya.company.service.BankAccountService;
import com.ataya.company.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Post: create a new bank account
 * Get: get bank account info by id
 * Get: get all bank accounts by store id
 * Put: update bank account info
 */

@RestController
@RequestMapping("/bank-account")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<BankAccountInfoResponse>> createBankAccount(SetBankAccountRequest createBankAccountRequest) {
        return ResponseEntity.status(201).body(bankAccountService.createBankAccount(createBankAccountRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountInfoResponse>> getBankAccount(String id) {
        return ResponseEntity.ok(bankAccountService.getBankAccount(id));
    }

    @GetMapping("/store/{storeId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<BankAccountInfoResponse>>> getAllBankAccountsByStoreId(String storeId) {
        return ResponseEntity.ok(bankAccountService.getAllBankAccountsByStoreId(storeId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<BankAccountInfoResponse>> updateBankAccount(String id, SetBankAccountRequest updateBankAccountRequest) {
        return ResponseEntity.ok(bankAccountService.updateBankAccount(id, updateBankAccountRequest));
    }

}
