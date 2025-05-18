package com.ataya.company.dto.bankAccount;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetBankAccountRequest {
    private String bankName;
    private String accountHolderName;
    private String iban;
    private String swiftCode;
    private String currency;
    private String storeId;
}
