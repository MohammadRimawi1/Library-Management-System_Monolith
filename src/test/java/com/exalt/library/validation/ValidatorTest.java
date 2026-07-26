package com.exalt.library.validation;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Validator}.
 * @author Mohammad Rimawi
 */
class ValidatorTest {

    /**
     * notBlank should return true for a non-empty string and false for null/blank/whitespace-only strings.
     */
    @Test
    void notBlank_validatesCorrectly() {
        assertTrue(Validator.notBlank("hello"));
        assertFalse(Validator.notBlank(null));
        assertFalse(Validator.notBlank(""));
        assertFalse(Validator.notBlank("   "));
    }

    /**
     * notNull should return true for any non-null object and false for null.
     */
    @Test
    void notNull_validatesCorrectly() {
        assertTrue(Validator.notNull("something"));
        assertFalse(Validator.notNull(null));
    }

    /**
     * size should return true only when the trimmed length falls within [min, max], and false for null.
     */
    @Test
    void size_validatesCorrectly() {
        assertTrue(Validator.size("hello", 1, 10));
        assertTrue(Validator.size("  hello  ", 1, 5));
        assertFalse(Validator.size("hi", 3, 10));
        assertFalse(Validator.size("hello world", 1, 5));
        assertFalse(Validator.size(null, 1, 10));
    }

    /**
     * between should return true only when the value falls within [min, max], inclusive.
     */
    @Test
    void between_validatesCorrectly() {
        assertTrue(Validator.between(5, 1, 10));
        assertTrue(Validator.between(1, 1, 10));
        assertTrue(Validator.between(10, 1, 10));
        assertFalse(Validator.between(0, 1, 10));
        assertFalse(Validator.between(11, 1, 10));
    }

    /**
     * matches should return true when the string matches the regex and false for null or a non-match.
     */
    @Test
    void matches_validatesCorrectly() {
        assertTrue(Validator.matches("abc123", "^[a-z]+\\d+$"));
        assertFalse(Validator.matches("ABC", "^[a-z]+\\d+$"));
        assertFalse(Validator.matches(null, "^[a-z]+\\d+$"));
    }

    /**
     * isValidObjectId should accept exactly 24 hex characters and reject anything else.
     */
    @Test
    void isValidObjectId_validatesCorrectly() {
        assertTrue(Validator.isValidObjectId("507f1f77bcf86cd799439011"));
        assertFalse(Validator.isValidObjectId("item1"));
        assertFalse(Validator.isValidObjectId("507f1f77bcf86cd79943"));
        assertFalse(Validator.isValidObjectId(null));
    }

    /**
     * isValidItemType should accept only the four known item type names.
     */
    @Test
    void isValidItemType_validatesCorrectly() {
        assertTrue(Validator.isValidItemType("BookPhysical"));
        assertTrue(Validator.isValidItemType("StoryPhysical"));
        assertTrue(Validator.isValidItemType("BookOnline"));
        assertTrue(Validator.isValidItemType("StoryOnline"));
        assertFalse(Validator.isValidItemType("Magazine"));
        assertFalse(Validator.isValidItemType(null));
    }

    /**
     * isValidEmail should accept well-formed emails and reject malformed ones or null.
     */
    @Test
    void isValidEmail_validatesCorrectly() {
        assertTrue(Validator.isValidEmail("test@test.com"));
        assertFalse(Validator.isValidEmail("not-an-email"));
        assertFalse(Validator.isValidEmail("missing@domain"));
        assertFalse(Validator.isValidEmail(null));
    }

    /**
     * isValidPhoneNumber should accept E.164-style numbers (with or without a leading +) and reject invalid formats.
     */
    @Test
    void isValidPhoneNumber_validatesCorrectly() {
        assertTrue(Validator.isValidPhoneNumber("+12345678"));
        assertTrue(Validator.isValidPhoneNumber("12345678"));
        assertFalse(Validator.isValidPhoneNumber("123"));
        assertFalse(Validator.isValidPhoneNumber("abcdefgh"));
        assertFalse(Validator.isValidPhoneNumber(null));
    }

    /**
     * isPastOrPresent should return true for today or any past date, and false for a future date or null.
     */
    @Test
    void isPastOrPresent_validatesCorrectly() {
        assertTrue(Validator.isPastOrPresent(LocalDate.now()));
        assertTrue(Validator.isPastOrPresent(LocalDate.now().minusDays(1)));
        assertFalse(Validator.isPastOrPresent(LocalDate.now().plusDays(1)));
        assertFalse(Validator.isPastOrPresent(null));
    }
}