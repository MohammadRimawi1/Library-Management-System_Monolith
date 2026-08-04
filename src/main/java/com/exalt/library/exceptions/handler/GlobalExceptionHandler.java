package com.exalt.library.exceptions.handler;

import com.exalt.library.exceptions.*;
import com.exalt.library.exceptions.notfound.BorrowerNotFoundException;
import com.exalt.library.exceptions.notfound.ItemNotFoundException;
import com.exalt.library.exceptions.notfound.ReservationNotFoundException;
import com.exalt.library.exceptions.notfound.UserNotFoundException;
import com.exalt.library.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
     * catches malformed input / invalid state cases (e.g. missing required fields, an item type that
     * can't do what was requested)
     * 400 status
     * @param e
     * @return
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException e) {
        return ResponseEntity.status(400).body(ApiResponse.error(400, "Bad Request", e.getMessage()));
    }

    /**
     * catches requests that are well-formed but conflict with the current state of a resource -
     * e.g. a duplicate entity, or a copy that's already taken
     * 409 status
     * @param e
     * @return
     */
    @ExceptionHandler({ConflictException.class, ItemUnavailableException.class})
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException e) {
        return ResponseEntity.status(409).body(ApiResponse.error(409, "Conflict", e.getMessage()));
    }

    /**
     * catches failed login attempts - the server doesn't recognize the credentials given
     * 401 status
     * @param e
     * @return
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationFailed(AuthenticationFailedException e) {
        return ResponseEntity.status(401).body(ApiResponse.error(401, "Unauthorized", e.getMessage()));
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
     * catches requests from an authenticated user who lacks permission for this action
     * 403 status
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(403).body(ApiResponse.error(403, "Forbidden", e.getMessage()));
    }
}