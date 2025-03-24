package com.ataya.contributor.exception.entry.point;

import com.ataya.contributor.util.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpcode(HttpServletResponse.SC_UNAUTHORIZED)
                .httpstatus("Unauthorized")
                .message("You are not authorized to access this resource, please login first.")
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .additionalDetails(null)
                .build();
        System.out.println(authException.getMessage());
        System.out.println(authException.getLocalizedMessage());
        System.out.println(authException.toString());
        System.out.println(authException.getStackTrace());
        System.out.println(authException.getCause());
        System.out.println(authException.getSuppressed());
        response.getWriter().write(errorResponse.toString());
    }
}
