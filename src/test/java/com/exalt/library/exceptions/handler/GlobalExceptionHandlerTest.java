package com.exalt.library.exceptions.handler;

import com.exalt.library.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link GlobalExceptionHandler}.
 * @author Mohammad Rimawi
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    /**
     * Reservation not found exceptions should return HTTP 404.
     */
    @Test
    void handleNotFound_returns404_forReservationNotFound() {
        ReservationNotFoundException ex = new ReservationNotFoundException("Reservation was not found");

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Reservation was not found", response.getBody().get("message"));
    }

    /**
     * User not found exceptions should return HTTP 404.
     */
    @Test
    void handleNotFound_returns404_forUserNotFound() {
        UserNotFoundException ex = new UserNotFoundException("User not found");

        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
    }

    /**
     * Illegal argument exceptions should return HTTP 400.
     */
    @Test
    void handleBadRequest_returns400_forIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");

        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Invalid input", response.getBody().get("message"));
    }

    /**
     * Illegal state exceptions should return HTTP 400.
     */
    @Test
    void handleBadRequest_returns400_forIllegalState() {
        IllegalStateException ex = new IllegalStateException("Reservation is not ready to be claimed");

        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);

        assertEquals(400, response.getStatusCode().value());
    }

    /**
     * Access denied exceptions should return HTTP 403.
     */
    @Test
    void handleAccessDenied_returns403() {
        AccessDeniedException ex = new AccessDeniedException("You do not have permission");

        ResponseEntity<Map<String, Object>> response = handler.handleAccessDenied(ex);

        assertEquals(403, response.getStatusCode().value());
        assertEquals("You do not have permission", response.getBody().get("message"));
    }

    /**
     * Requests for missing resources should return HTTP 404.
     */
    @Test
    void handleNoResourceFound_returns404() {
        NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET, "some/uri", "some/path");

        ResponseEntity<Map<String, Object>> response = handler.handleNoResourceFound(ex);

        assertEquals(404, response.getStatusCode().value());
    }

    /**
     * Unexpected exceptions should return HTTP 500.
     */
    @Test
    void handleGeneric_returns500_forUnexpectedException() {
        RuntimeException ex = new RuntimeException("Something broke");

        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);

        assertEquals(500, response.getStatusCode().value());
    }
}