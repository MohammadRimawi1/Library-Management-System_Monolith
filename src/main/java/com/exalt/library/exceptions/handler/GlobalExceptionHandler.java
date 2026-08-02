package com.exalt.library.exceptions.handler;

import com.exalt.library.exceptions.*;
import com.exalt.library.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * a class representing the exception handling for common possible errors
 * @author Mohammad Rimawi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * catches "not found" cases across reservations, borrowers, and items
     * 404 status
     * @param e
     * @return
     */
    @ExceptionHandler({ReservationNotFoundException.class, BorrowerNotFoundException.class, ItemNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
        return ResponseEntity.status(404).body(ApiResponse.error(404, "Not Found", e.getMessage()));
    }

    /**
     * catches bad input / invalid state cases (e.g. claiming a non-READY reservation, or an item with no copies left)
     * 400 status
     * @param e
     * @return
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, ItemUnavailableException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException e) {
        return ResponseEntity.status(400).body(ApiResponse.error(400, "Bad Request", e.getMessage()));
    }

    /**
     * catch-all for anything unexpected
     * 500 status
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception e) {
        return ResponseEntity.status(500).body(ApiResponse.error(500, "Internal Server Error", e.getMessage()));
    }

    /**
     * catches validation failures from @Valid on request DTOs
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(400).body(ApiResponse.error(400, "Bad Request", message));
    }

    /**
     * catches method argument fails type coercion
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "Invalid value '" + e.getValue() + "' for parameter '" + e.getName() + "'";
        return ResponseEntity.status(400).body(ApiResponse.error(400, "Bad Request", message));
    }

    /**
     * catches access denied errors when a user lacks required authorities or roles
     * 403 status
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(403).body(ApiResponse.error(403, "Forbidden", e.getMessage()));
    }

    /**
     * catches static resource or endpoint path resolution failures
     * 404 status
     * @param e
     * @return
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException e) {
        return ResponseEntity.status(404).body(ApiResponse.error(404, "Not Found", "The requested resource does not exist"));
    }
}