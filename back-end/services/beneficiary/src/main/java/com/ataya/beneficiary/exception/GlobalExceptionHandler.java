package com.ataya.beneficiary.exception;

import com.ataya.beneficiary.exception.custom.*;
import com.ataya.contributor.util.ErrorResponse;
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
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .httpcode(HttpStatus.NOT_FOUND.value())
                .httpstatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .additionalDetails(ex.getAdditionalDetails())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .httpcode(HttpStatus.BAD_REQUEST.value())
                .httpstatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .additionalDetails(ex.getAdditionalDetails())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .httpcode(HttpStatus.CONFLICT.value())
                .httpstatus(HttpStatus.CONFLICT.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .additionalDetails(ex.getAdditionalDetails())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperationException(InvalidOperationException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .httpcode(HttpStatus.BAD_REQUEST.value())
                .httpstatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .additionalDetails(ex.getAdditionalDetails())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleExternalServiceException(ExternalServiceException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .httpcode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .httpstatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .additionalDetails(ex.getAdditionalDetails())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceededException(RateLimitExceededException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .httpcode(HttpStatus.TOO_MANY_REQUESTS.value())
                .httpstatus(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .additionalDetails(ex.getAdditionalDetails())
                .build();
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation error")
                .httpcode(HttpStatus.BAD_REQUEST.value())
                .httpstatus(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .additionalDetails(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .httpcode(HttpStatus.NOT_FOUND.value())
                .httpstatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .additionalDetails(ex.getAdditionalDetails())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .httpcode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .httpstatus(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .method(((ServletWebRequest) request).getRequest().getMethod())
                .additionalDetails(ex.getAdditionalDetails())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
