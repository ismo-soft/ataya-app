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
    private String accountHolderName;
    private String iban;
    private String swiftCode;
    private String currency;
    private String storeId;
}
