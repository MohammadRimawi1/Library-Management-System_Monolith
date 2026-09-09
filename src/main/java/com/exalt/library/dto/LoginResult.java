package com.exalt.library.dto;

import com.exalt.library.models.users.User;

/**
 * A record representing the Data Transfer Object for a Login result.
 * @param token
 * @param user
 */
public record LoginResult(String token, User user) {
}
