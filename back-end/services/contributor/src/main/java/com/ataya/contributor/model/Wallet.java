package com.ataya.contributor.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Wallet {

    @Id
    private String id;
    private String name;
    private Double balance;
    private String currency;
    private String ownerId;

}
