package com.ataya.inventory.model;

import com.ataya.inventory.enums.MovementType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class StockMovement {
    @Id
    private String id;
    private String inventoryId;
    private String storeId;
    private MovementType type;
    private Double quantity;
    private String note;
    private String reason;
    private String referenceId;
    private LocalDateTime happenedAt;
    private String by;
}
