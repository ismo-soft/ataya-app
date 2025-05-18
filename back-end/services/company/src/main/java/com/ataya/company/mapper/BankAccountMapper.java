package com.ataya.company.mapper;

import com.ataya.company.dto.bankAccount.BankAccountInfoResponse;
import com.ataya.company.dto.bankAccount.SetBankAccountRequest;
import com.ataya.company.model.BankAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountInfoResponse toBankAccountInfoResponse(BankAccount bankAccount);
    BankAccount toBankAccount(SetBankAccountRequest setBankAccountRequest);
}
