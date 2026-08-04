package com.exalt.library.dto;

import java.time.LocalDate;

/**
 * A record representing the Data Transfer Object for an author.
 * @param name the name of the author
 * @param nationality the nationality of the author
 * @param birthDate the birthdate of an author
 * @author Mohammad Rimawi
 */
public record AuthorDTO(
        String name,
        String nationality,
        LocalDate birthDate
) {
    /**
     * compact constructor - trims name and nationality automatically instead of
     * rejecting values with leading/trailing whitespace
     */
    public AuthorDTO {
        if (name != null) {
            name = name.trim();
        }
        if (nationality != null) {
            nationality = nationality.trim();
        }
    }
}