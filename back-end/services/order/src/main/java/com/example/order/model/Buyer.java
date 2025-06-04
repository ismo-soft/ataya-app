package com.example.order.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Buyer {
    private String buyerId;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private String nationalId;
}
