//package com.exalt.library.validation;
//
//import com.exalt.library.dto.AuthorDTO;
//import com.exalt.library.dto.LibraryItemDTO;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.time.LocalDate;
//
///**
// * Unit tests for {@link LibraryItemValidator}.
// * @author Mohammad Rimawi
// */
//public class LibraryItemValidatorTest {
//    /**
//     * Valid library item data should pass validation.
//     */
//    @Test
//    void  validate_doesNotThrow_whenAllFieldsValid() {
//        LibraryItemDTO dto = new LibraryItemDTO("BookPhysical", "Computer Engineering",
//                2, "A Book that teaches the basics of CE", "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertDoesNotThrow(() -> LibraryItemValidator.validate(dto));
//    }
//
//    /**
//     * Blank item types should be rejected.
//     */
//    @Test
//    void validate_throws_whenTypeIsEmpty() {
//        LibraryItemDTO dto = new LibraryItemDTO("", "Computer Engineering",
//                2, "A Book that teaches the basics of CE", "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertThrows(IllegalArgumentException.class ,() -> LibraryItemValidator.validate(dto));
//    }
//
//    /**
//     * Blank item titles should be rejected.
//     */
//    @Test
//    void validate_throws_whenTitleIsEmpty() {
//        LibraryItemDTO dto = new LibraryItemDTO("BookPhysical", "",
//                2, "A Book that teaches the basics of CE", "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertThrows(IllegalArgumentException.class ,() -> LibraryItemValidator.validate(dto));
//    }
//
//    /**
//     * Titles shorter than the minimum length should be rejected.
//     */
//    @Test
//    void validate_throws_whenTitleTooShort() {
//        LibraryItemDTO dto = new LibraryItemDTO("BookPhysical", "A",
//                2, "A Book that teaches the basics of CE", "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertThrows(IllegalArgumentException.class ,() -> LibraryItemValidator.validate(dto));
//    }
//
//    /**
//     * New Type that doesn't exist should be rejected
//     */
//    @Test
//    void validate_throws_whenTypeDoesntExist() {
//        LibraryItemDTO dto = new LibraryItemDTO("NewType", "Africans",
//                2, "A Book that speaks about the history of Africans", "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertThrows(IllegalArgumentException.class ,() -> LibraryItemValidator.validate(dto));
//    }
//
//    /**
//     * Not valid number of copies should be rejected
//     */
//    @Test
//    void validate_throws_whenNumOfCopiesNotValid() {
//        LibraryItemDTO dto = new LibraryItemDTO("BookPhysical", "Africans",
//                5555, "A Book that speaks about the history of Africans", "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertThrows(IllegalArgumentException.class ,() -> LibraryItemValidator.validate(dto));
//    }
//
//    /**
//     * Not valid size description should be rejected
//     */
//    @Test
//    void validate_throws_whenDescriptionNotValid() {
//        String tooLongDescription = "a".repeat(2001);
//
//        LibraryItemDTO dto = new LibraryItemDTO("BookPhysical", "Africans",
//                24,
//                tooLongDescription,
//                "English",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertThrows(IllegalArgumentException.class ,() -> LibraryItemValidator.validate(dto));
//    }
//
//    /**
//     * Not valid size language should be rejected
//     */
//    @Test
//    void validate_throws_whenLanguageNotValid() {
//        LibraryItemDTO dto = new LibraryItemDTO("BookPhysical", "Africans",
//                24,
//                "A Book that speaks about the history of Africans",
//                "E",
//                new AuthorDTO("John Smith", "Canadian", LocalDate.of(1980, 1, 1)));
//
//        assertThrows(IllegalArgumentException.class ,() -> LibraryItemValidator.validate(dto));
//    }
//
//    /**
//     * null author should be rejected
//     */
//    @Test
//    void validate_throws_whenAuthorNull() {
//        LibraryItemDTO dto = new LibraryItemDTO("BookPhysical", "Africans",
//                24,
//                "A Book that speaks about the history of Africans",
//                "E",
//                null);
//
//        assertThrows(IllegalArgumentException.class ,() -> LibraryItemValidator.validate(dto));
//    }
//}
