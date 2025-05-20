package com.ataya.inventory.util;

import com.ataya.inventory.dto.InventoryStatistics;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private int statusCode;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private int page;
    private int size;
    private long total;
    private int totalPages;
    private T data;
    private InventoryStatistics statistics;
}
