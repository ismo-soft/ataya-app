package com.ataya.company.service.impl;

import com.ataya.company.dto.bankAccount.BankAccountInfoResponse;
import com.ataya.company.dto.bankAccount.SetBankAccountRequest;
import com.ataya.company.exception.custom.InvalidOperationException;
import com.ataya.company.exception.custom.ResourceNotFoundException;
import com.ataya.company.mapper.BankAccountMapper;
import com.ataya.company.model.BankAccount;
import com.ataya.company.repo.BankAccountRepository;
import com.ataya.company.service.BankAccountService;
import com.ataya.company.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    @Override
    public ApiResponse<BankAccountInfoResponse> createBankAccount(SetBankAccountRequest createBankAccountRequest) {
        boolean isExist = bankAccountRepository.existsByStoreId(createBankAccountRequest.getStoreId());
        if (isExist) {
            throw new InvalidOperationException("Bank Account Creation","You already have a bank account for this store");
        }
        BankAccount bankAccount = bankAccountMapper.toBankAccount(createBankAccountRequest);
        bankAccountRepository.save(bankAccount);

        return ApiResponse.<BankAccountInfoResponse>builder()
                .message("Bank account created successfully")
                .status(HttpStatus.CREATED.getReasonPhrase())
                .statusCode(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(bankAccountMapper.toBankAccountInfoResponse(bankAccount))
                .build();

    }

    @Override
    public ApiResponse<BankAccountInfoResponse> getBankAccount(String id) {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BankAccount", "id", id)
        );
        return ApiResponse.<BankAccountInfoResponse>builder()
                .message("Bank account retrieved successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(bankAccountMapper.toBankAccountInfoResponse(bankAccount))
                .build();
    }

    @Override
    public ApiResponse<List<BankAccountInfoResponse>> getAllBankAccountsByStoreId(String storeId) {
        List<BankAccount> bankAccounts = bankAccountRepository.findAllByStoreId(storeId);
        if (bankAccounts.isEmpty()) {
            throw new ResourceNotFoundException("BankAccount", "storeId", storeId);
        }
        List<BankAccountInfoResponse> bankAccountInfoResponses = bankAccounts.stream()
                .map(bankAccountMapper::toBankAccountInfoResponse)
                .toList();
        return ApiResponse.<List<BankAccountInfoResponse>>builder()
                .message("Bank accounts retrieved successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(bankAccountInfoResponses)
                .build();
    }

    @Override
    public ApiResponse<BankAccountInfoResponse> updateBankAccount(String id, SetBankAccountRequest updateBankAccountRequest) {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BankAccount", "id", id)
        );
        bankAccount.setBankName(updateBankAccountRequest.getBankName());
        bankAccount.setAccountHolderName(updateBankAccountRequest.getAccountHolderName());
        bankAccount.setIban(updateBankAccountRequest.getIban());
        bankAccount.setSwiftCode(updateBankAccountRequest.getSwiftCode());
        bankAccount.setCurrency(updateBankAccountRequest.getCurrency());

        bankAccountRepository.save(bankAccount);
        return ApiResponse.<BankAccountInfoResponse>builder()
                .message("Bank account updated successfully")
                .status(HttpStatus.OK.getReasonPhrase())
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(bankAccountMapper.toBankAccountInfoResponse(bankAccount))
                .build();
    }
}
