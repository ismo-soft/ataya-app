package com.ataya.company.exception.entry.point;

import com.ataya.company.util.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpcode(HttpStatus.FORBIDDEN.value())
                .httpstatus(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message("You are not authorized to access this resource")
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .additionalDetails(null)
                .build();
        response.getWriter().write(errorResponse.toString()

        );
    }
}
