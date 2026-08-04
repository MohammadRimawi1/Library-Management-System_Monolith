package com.exalt.library.exceptions.handler;

import com.exalt.library.exceptions.*;
import com.exalt.library.exceptions.notfound.BorrowerNotFoundException;
import com.exalt.library.exceptions.notfound.ItemNotFoundException;
import com.exalt.library.exceptions.notfound.ReservationNotFoundException;
import com.exalt.library.exceptions.notfound.UserNotFoundException;
import com.exalt.library.util.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    /**
     * catches a lost-update race - two requests tried to modify the same document at once,
     * and this one lost
     * 409 status
     * @param e
     * @return
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLock(OptimisticLockingFailureException e) {
        return ResponseEntity.status(409).body(ApiResponse.error(409, "Conflict",
                "This copy was just taken by someone else — please try again"));
    }

    /**
     * catches a database-level unique-index violation - this is the safety net for when two
     * concurrent requests both pass the app-level duplicate check before either one saves
     * 409 status
     * @param e
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKey(DuplicateKeyException e) {
        return ResponseEntity.status(409).body(ApiResponse.error(409, "Conflict",
                "A record with this value already exists"));
    }

    /**
     * catches malformed/unparseable JSON request bodies - e.g. broken syntax, or a field
     * that can't be coerced into the expected type (bad date format, wrong type, etc.)
     * 400 status
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleMalformedJson(HttpMessageNotReadableException e) {
        return ResponseEntity.status(400).body(ApiResponse.error(400, "Bad Request",
                "The request body is malformed or contains an invalid value"));
    }

    /**
     * catches database-level integrity/schema violations (e.g. a $jsonSchema rejection).
     * intentionally hides the raw MongoDB error details from the client - those are
     * internal database structure, not something a client should see - but this is still
     * a real server-side bug, so it stays a 500
     * @param e
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.status(500).body(ApiResponse.error(500, "Internal Server Error",
                "A database error occurred while processing your request"));
    }

    /**
     * catches an internal type-mismatch bug - a strategy was resolved against the wrong
     * item type. Shouldn't happen given BorrowStrategyFactory's instanceof check, but this
     * is a defensive net so it fails as a clean 500 instead of a raw cast-exception message
     * @param e
     * @return
     */
    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<Map<String, Object>> handleClassCastException(ClassCastException e) {
        return ResponseEntity.status(500).body(ApiResponse.error(500, "Internal Server Error",
                "An unexpected internal error occurred"));
    }
}