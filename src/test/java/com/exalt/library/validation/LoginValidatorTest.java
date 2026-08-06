//package com.exalt.library.validation;
//
//import com.exalt.library.dto.LoginDTO;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for {@link LoginValidator}.
// * @author Mohammad Rimawi
// */
//public class LoginValidatorTest {
//
//    /**
//     * Valid login data should pass validation.
//     */
//    @Test
//    void validate_doesNotThrow_whenAllFieldsValid() {
//        LoginDTO dto = new LoginDTO("test@gmail.com", "password123");
//
//        assertDoesNotThrow(() -> LoginValidator.validate(dto));
//    }
//
//    /**
//     * Blank email should be rejected
//     */
//    @Test
//    void validate_throws_whenEmailBlank() {
//        LoginDTO dto = new LoginDTO("", "password123");
//
//        assertThrows(IllegalArgumentException.class, () -> LoginValidator.validate(dto));
//    }
//
//    /**
//     * Blank Password should be rejected
//     */
//    @Test
//    void validate_throws_whenPasswordBlank() {
//        LoginDTO dto = new LoginDTO("test@gmail.com", "");
//
//        assertThrows(IllegalArgumentException.class, () -> LoginValidator.validate(dto));
//    }
//}
