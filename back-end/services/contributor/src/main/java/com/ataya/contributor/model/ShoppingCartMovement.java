package com.ataya.contributor.model;

import com.ataya.contributor.enums.ShoppingCartMovementType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ShoppingCartMovement {

    @Id
    private String id;
    private String itemId;
    private String shoppingCartId;
    private ShoppingCartMovementType movement;
    private String happenedAt;
}
