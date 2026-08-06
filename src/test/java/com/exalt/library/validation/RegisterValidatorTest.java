//package com.exalt.library.validation;
//
//import com.exalt.library.dto.RegisterDTO;
//import com.exalt.library.models.users.Role;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for {@link RegisterValidator}.
// * @author Mohammad Rimawi
// */
//public class RegisterValidatorTest {
//
//    /**
//     * Valid register data should pass validation. (for a borrower)
//     */
//    @Test
//    void validate_doesNotThrow_whenAllFieldsValidBorrower() {
//        RegisterDTO dto = new RegisterDTO("Malik", "test@gmail.com",
//                "password123", "+97055555", null);
//
//        assertDoesNotThrow(() -> RegisterValidator.validate(dto, Role.BORROWER));
//    }
//
//    /**
//     * Valid register data should pass validation. (for a Librarian)
//     */
//    @Test
//    void validate_doesNotThrow_whenAllFieldsValidLibrarian() {
//        RegisterDTO dto = new RegisterDTO("Malik", "test@gmail.com",
//                "password123", "+97055555", "real-secret-value");
//
//        assertDoesNotThrow(() -> RegisterValidator.validate(dto, Role.LIBRARIAN));
//    }
//
//    /**
//     * Blank Email Should be rejected
//     */
//    @Test
//    void validate_throws_whenEmailBlank() {
//        RegisterDTO dto = new RegisterDTO("Malik", "",
//                "password123", "+97055555", null);
//
//        assertThrows(IllegalArgumentException.class, () -> RegisterValidator.validate(dto, Role.BORROWER));
//    }
//
//    /**
//     * Not Valid Email Should be rejected
//     */
//    @Test
//    void validate_throws_whenEmailNotValid() {
//        RegisterDTO dto = new RegisterDTO("Malik", "asfasgfag",
//                "password123", "+97055555", null);
//
//        assertThrows(IllegalArgumentException.class, () -> RegisterValidator.validate(dto, Role.BORROWER));
//    }
//
//    /**
//     * Blank Password Should be rejected
//     */
//    @Test
//    void validate_throws_whenPasswordBlank() {
//        RegisterDTO dto = new RegisterDTO("Malik", "test@gmail.com",
//                "", "+97055555", null);
//
//        assertThrows(IllegalArgumentException.class, () -> RegisterValidator.validate(dto, Role.BORROWER));
//    }
//
//    /**
//     * Not valid Password Should be rejected
//     */
//    @Test
//    void validate_throws_whenPasswordNotValid() {
//        RegisterDTO dto = new RegisterDTO("Malik", "test@gmail.com",
//                "asqf", "+97055555", null);
//
//        assertThrows(IllegalArgumentException.class, () -> RegisterValidator.validate(dto, Role.BORROWER));
//    }
//
//    /**
//     * Blank Name Should be rejected
//     */
//    @Test
//    void validate_throws_whenBorrowerNameBlank() {
//        RegisterDTO dto = new RegisterDTO("", "test@gmail.com",
//                "password123", "+97055555", null);
//
//        assertThrows(IllegalArgumentException.class, () -> RegisterValidator.validate(dto, Role.BORROWER));
//    }
//
//    /**
//     * Not Valid Name Should be rejected
//     */
//    @Test
//    void validate_throws_whenBorrowerNameNotValid() {
//        RegisterDTO dto = new RegisterDTO("A", "test@gmail.com",
//                "password123", "+97055555", null);
//
//        assertThrows(IllegalArgumentException.class, () -> RegisterValidator.validate(dto, Role.BORROWER));
//    }
//
//    /**
//     * Not Valid phone number Should be rejected
//     */
//    @Test
//    void validate_throws_whenPhoneNumberNotValid() {
//        RegisterDTO dto = new RegisterDTO("Ahmad", "test@gmail.com",
//                "password123", "12ras214", null);
//
//        assertThrows(IllegalArgumentException.class, () -> RegisterValidator.validate(dto, Role.BORROWER));
//    }
//}
