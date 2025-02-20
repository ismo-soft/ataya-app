package com.ataya.company.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "bank-account")
public class BankAccount {
    @Id
    private String id;
    private String bankName;
    private String accountNumber;
    private String iban;
    private String swiftCode;
    private String branchCode;
    private String branchName;
    private String accountName;
    private String currency;
    private String companyId;
    private String workerId;
    private String storeId;
    private String managerId;
}
