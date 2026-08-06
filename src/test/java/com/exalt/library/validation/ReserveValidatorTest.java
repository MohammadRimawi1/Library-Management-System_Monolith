//package com.exalt.library.validation;
//
//import com.exalt.library.dto.ReserveDTO;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for {@link ReserveValidator}.
// * @author Mohammad Rimawi
// */
//class ReserveValidatorTest {
//
//    /**
//     * A valid item ID should pass validation.
//     */
//    @Test
//    void validate_doesNotThrow_whenItemIdValid() {
//        ReserveDTO dto = new ReserveDTO("507f1f77bcf86cd799439011");
//
//        assertDoesNotThrow(() -> ReserveValidator.validate(dto));
//    }
//
//    /**
//     * Blank item IDs should be rejected.
//     */
//    @Test
//    void validate_throws_whenItemIdBlank() {
//        ReserveDTO dto = new ReserveDTO("");
//
//        assertThrows(IllegalArgumentException.class, () -> ReserveValidator.validate(dto));
//    }
//
//    /**
//     * Item IDs with an invalid format should be rejected.
//     */
//    @Test
//    void validate_throws_whenItemIdWrongFormat() {
//        ReserveDTO dto = new ReserveDTO("not-a-real-id");
//
//        assertThrows(IllegalArgumentException.class, () -> ReserveValidator.validate(dto));
//    }
//
//    /**
//     * Item IDs shorter than the required length should be rejected.
//     */
//    @Test
//    void validate_throws_whenItemIdTooShort() {
//        ReserveDTO dto = new ReserveDTO("abc123");
//
//        assertThrows(IllegalArgumentException.class, () -> ReserveValidator.validate(dto));
//    }
//}