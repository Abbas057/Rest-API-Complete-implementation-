package com.example.productapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Creates or propagates a correlation ID for each HTTP request
 * and stores it in the logging MDC.
 */
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID =
            "X-Correlation-ID";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String correlationId =
                request.getHeader(CORRELATION_ID);

        if (correlationId == null ||
                correlationId.isBlank()) {

            correlationId =
                    UUID.randomUUID().toString();
        }

        response.setHeader(
                CORRELATION_ID,
                correlationId
        );

        try {

            MDC.put(CORRELATION_ID, correlationId);

            filterChain.doFilter(
                    request,
                    response
            );

        } finally {

            MDC.remove(CORRELATION_ID);
        }
    }
}