package com.exalt.library.controllers;

import com.exalt.library.dto.UserDTO;
import com.exalt.library.models.users.Role;
import com.exalt.library.models.users.User;
import com.exalt.library.services.UserServices;
import com.exalt.library.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * an admin controller that gets a request from the client, does a specific job then returns the response
 * routes here are locked down to ADMIN only, at the SecurityConfig level
 * @author Mohammad Rimawi
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserServices userServices; // Defines the user services

    /**
     * constructor injection
     * @param userServices
     */
    public AdminController(UserServices userServices) {
        this.userServices = userServices;
    }

    /**
     * a method for listing users, optionally filtered by role
     * exists on: /api/admin/users?role=BORROWER
     * @param role
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> findUsers(@RequestParam(required = false) Role role) {
        List<UserDTO> users = userServices.getUsers(role).stream()
                .map(UserDTO::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(200, users));
    }

    /**
     * a method for promoting a borrower to librarian
     * exists on: /api/admin/users/{userId}/promote-to-librarian
     * @param userId
     * @return
     */
    @PatchMapping("/users/{userId}/promote-to-librarian")
    public ResponseEntity<Map<String, Object>> promoteToLibrarian(@PathVariable String userId) {
        User user = userServices.promoteToLibrarian(userId);
        return ResponseEntity.ok(ApiResponse.success(200, user));
    }

    /**
     * a method for demoting a librarian back to borrower
     * exists on: /api/admin/users/{userId}/demote-to-borrower
     * @param userId
     * @return
     */
    @PatchMapping("/users/{userId}/demote-to-borrower")
    public ResponseEntity<Map<String, Object>> demoteToBorrower(@PathVariable String userId) {
        User user = userServices.demoteToBorrower(userId);
        return ResponseEntity.ok(ApiResponse.success(200, user));
    }
}