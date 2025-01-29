package com.wastech.cv_builder_api.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException {

        // Log the start of the method
        logger.debug("Commence method started");

        // Log the unauthorized error message
        logger.error("Unauthorized error: {}", authException.getMessage());

        // Set the response content type to JSON
        logger.debug("Setting response content type to {}", MediaType.APPLICATION_JSON_VALUE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Set the response status to 401 (Unauthorized)
        logger.debug("Setting response status to 401 (Unauthorized)");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Create a map to hold the response body
        logger.debug("Creating response body map");
        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        // Log the constructed response body
        logger.debug("Response body: {}", body);

        // Create an ObjectMapper for writing the response body
        logger.debug("Creating ObjectMapper");
        final ObjectMapper mapper = new ObjectMapper();

        // Write the response body to the output stream
        logger.debug("Writing response body to output stream");
        mapper.writeValue(response.getOutputStream(), body);

        // Log the end of the method
        logger.debug("Commence method ended");
    }

}

