package com.exalt.library.dto;

import com.exalt.library.models.users.Role;
import com.exalt.library.models.users.User;

/**
 * A record representing the Data Transfer Object for a user account,
 * safe to expose over the API - never includes the password hash.
 * @param id the user's id
 * @param email the user's email
 * @param role the user's role
 * @param name the user's name
 * @author Mohammad Rimawi
 */
public record UserDTO(
        String id,
        String email,
        Role role,
        String name
) {
    /**
     * builds a UserDTO from a User entity
     * @param user
     * @return
     */
    public static UserDTO from(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getRole(), user.getName());
    }
}