package com.example.order.exception;

import com.example.order.exception.custom.*;
import com.example.order.util.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse response = buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ((ServletWebRequest) request).getRequest().getMethod(),
                ex.getAdditionalDetails()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    private ErrorResponse buildErrorResponse(String message, HttpStatus httpStatus, String requestURI, String method, Map<String, Object> additionalDetails) {
        return ErrorResponse.builder()
                .message(message)
                .httpcode(httpStatus.value())
                .httpstatus(httpStatus.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(requestURI)
                .method(method)
                .additionalDetails(additionalDetails)
                .build();
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        ErrorResponse response = buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ((ServletWebRequest) request).getRequest().getMethod(),
                ex.getAdditionalDetails()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        ErrorResponse response = buildErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ((ServletWebRequest) request).getRequest().getMethod(),
                ex.getAdditionalDetails()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperationException(InvalidOperationException ex, WebRequest request) {
        ErrorResponse response = buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ((ServletWebRequest) request).getRequest().getMethod(),
                ex.getAdditionalDetails()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(ExternalServiceException ex, WebRequest request) {

        ErrorResponse response = buildErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ((ServletWebRequest) request).getRequest().getMethod(),
                ex.getAdditionalDetails()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceededException(RateLimitExceededException ex, WebRequest request) {

        ErrorResponse response = buildErrorResponse(
                ex.getMessage(),
                HttpStatus.TOO_MANY_REQUESTS,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ((ServletWebRequest) request).getRequest().getMethod(),
                ex.getAdditionalDetails()
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse response = buildErrorResponse(
                "Validation error",
                HttpStatus.BAD_REQUEST,
                null,
                null,
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
        ErrorResponse response = buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ((ServletWebRequest) request).getRequest().getMethod(),
                ex.getAdditionalDetails()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex, WebRequest request) {

        ErrorResponse response = buildErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ((ServletWebRequest) request).getRequest().getRequestURI(),
                ((ServletWebRequest) request).getRequest().getMethod(),
                ex.getAdditionalDetails()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
