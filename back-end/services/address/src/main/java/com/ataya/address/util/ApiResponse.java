package com.ataya.address.util;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ApiResponse {
    private int statusCode;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private int page;
    private int size;
    private int total;
    private int totalPages;
    private Object data;
}
