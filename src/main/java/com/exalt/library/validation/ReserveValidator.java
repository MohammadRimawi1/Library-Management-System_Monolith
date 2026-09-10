package com.exalt.library.validation;

import com.exalt.library.dto.ReserveDTO;

/**
 * a class for validating reservation fields
 * @author Mohammad Rimawi
 */
public class ReserveValidator {

    /**
     * a private constructor to prevent instantiation
     */
    private ReserveValidator() {}

    /**
     * a static method that validates
     * @param reserveDTO
     */
    public static void validate(ReserveDTO reserveDTO) {
        if(!Validator.notBlank(reserveDTO.itemId())) {
            throw new IllegalArgumentException("ItemId is required");
        }

        if(!Validator.isValidObjectId(reserveDTO.itemId())) {
            throw new IllegalArgumentException("ItemId must be valid");
        }
    }
}
