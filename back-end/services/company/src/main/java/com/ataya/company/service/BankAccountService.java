package com.ataya.company.service;

import com.ataya.company.dto.bankAccount.BankAccountInfoResponse;
import com.ataya.company.dto.bankAccount.SetBankAccountRequest;
import com.ataya.company.util.ApiResponse;

import java.util.List;

public interface BankAccountService {
    ApiResponse<BankAccountInfoResponse> createBankAccount(SetBankAccountRequest createBankAccountRequest);

    ApiResponse<BankAccountInfoResponse> getBankAccount(String id);

    ApiResponse<List<BankAccountInfoResponse>> getAllBankAccountsByStoreId(String storeId);

    ApiResponse<BankAccountInfoResponse> updateBankAccount(String id, SetBankAccountRequest updateBankAccountRequest);
}
