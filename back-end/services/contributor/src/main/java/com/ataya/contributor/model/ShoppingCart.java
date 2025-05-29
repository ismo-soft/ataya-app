package com.ataya.contributor.model;


import com.ataya.contributor.enums.ShoppingCartStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ShoppingCart {

    @Id
    private String id;
    private String customerId;
    private String createdAt;
    private String updatedAt;
    private String closedAt;
    private ShoppingCartStatus status;
    private String totalAmount;
    private String items;


}
