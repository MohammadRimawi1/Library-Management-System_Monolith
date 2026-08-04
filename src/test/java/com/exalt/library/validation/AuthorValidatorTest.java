//package com.exalt.library.validation;
//
//import com.exalt.library.dto.AuthorDTO;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for {@link AuthorValidator}.
// * @author Mohammad Rimawi
// */
//class AuthorValidatorTest {
//
//    /**
//     * Valid author data should pass validation.
//     */
//    @Test
//    void validate_doesNotThrow_whenAllFieldsValid() {
//        AuthorDTO dto = new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1));
//
//        assertDoesNotThrow(() -> AuthorValidator.validate(dto));
//    }
//
//    /**
//     * A null birthdate is allowed.
//     */
//    @Test
//    void validate_doesNotThrow_whenBirthDateIsNull() {
//        AuthorDTO dto = new AuthorDTO("John Smith", "Canadian", null);
//
//        assertDoesNotThrow(() -> AuthorValidator.validate(dto));
//    }
//
//    /**
//     * Blank author names should be rejected.
//     */
//    @Test
//    void validate_throws_whenNameIsBlank() {
//        AuthorDTO dto = new AuthorDTO("", "Canadian", null);
//
//        assertThrows(IllegalArgumentException.class, () -> AuthorValidator.validate(dto));
//    }
//
//    /**
//     * Names shorter than the minimum length should be rejected.
//     */
//    @Test
//    void validate_throws_whenNameTooShort() {
//        AuthorDTO dto = new AuthorDTO("A", "Canadian", null);
//
//        assertThrows(IllegalArgumentException.class, () -> AuthorValidator.validate(dto));
//    }
//
//    /**
//     * Blank nationalities should be rejected.
//     */
//    @Test
//    void validate_throws_whenNationalityIsBlank() {
//        AuthorDTO dto = new AuthorDTO("John Smith", "", null);
//
//        assertThrows(IllegalArgumentException.class, () -> AuthorValidator.validate(dto));
//    }
//
//    /**
//     * Future birthdate should be rejected.
//     */
//    @Test
//    void validate_throws_whenBirthDateInFuture() {
//        AuthorDTO dto = new AuthorDTO("John Smith", "Canadian", LocalDate.now().plusDays(1));
//
//        assertThrows(IllegalArgumentException.class, () -> AuthorValidator.validate(dto));
//    }
//}