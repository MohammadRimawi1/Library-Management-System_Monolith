package com.exalt.library.validation;

import com.exalt.library.dto.LibraryItemDTO;

/**
 * a class for validating libraryitem fields
 * @author Mohammad Rimawi
 */
public class LibraryItemValidator {
    /**
     * a private constructor to prevent instantiation
     */
    private LibraryItemValidator() {}

    /**
     * a static method that validates
     * @param libraryItemDTO
     */
    public static void validate(LibraryItemDTO libraryItemDTO) {
        if(!Validator.notBlank(libraryItemDTO.type())) {
            throw new IllegalArgumentException("Type is required");
        }
        if(!Validator.notBlank(libraryItemDTO.title())) {
            throw new IllegalArgumentException("Title is required");
        }

        if(!Validator.size(libraryItemDTO.title(), 2, 150)) {
            throw new IllegalArgumentException("Title must be between 1 and 150 characters");
        }

        if (!Validator.isValidItemType(libraryItemDTO.type())) {
            throw new IllegalArgumentException("Type must be one of: BookPhysical, StoryPhysical, BookOnline, StoryOnline");
        }

        if (libraryItemDTO.numOfCopies() != null && !Validator.between(libraryItemDTO.numOfCopies(), 0, 1000)) {
            throw new IllegalArgumentException("Enter a valid number of copies");
        }

        if (libraryItemDTO.description() != null && !Validator.size(libraryItemDTO.description(), 0, 1000)) {
            throw new IllegalArgumentException("Description must not exceed 2000 characters");
        }

        if (libraryItemDTO.language() != null && !Validator.size(libraryItemDTO.language(), 2, 50)) {
            throw new IllegalArgumentException("Language must be between 2 and 50 characters");
        }

        if (libraryItemDTO.edition() != null && !Validator.size(libraryItemDTO.edition(), 1, 50)) {
            throw new IllegalArgumentException("Edition must be between 1 and 50 characters");
        }

        if (libraryItemDTO.image() != null && !Validator.size(libraryItemDTO.image(), 0, 700)) {
            throw new IllegalArgumentException("Image path/URL must not exceed 700 characters");
        }

        if(!Validator.notNull(libraryItemDTO.author())) {
            throw new IllegalArgumentException("Author shouldn't be null");
        }

        AuthorValidator.validate(libraryItemDTO.author());

    }
}
