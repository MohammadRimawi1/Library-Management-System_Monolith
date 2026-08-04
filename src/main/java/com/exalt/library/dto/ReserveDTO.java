package com.exalt.library.dto;

/**
 * A record representing the Data Transfer Object for a reservation.
 * @author Mohammad Rimawi
 * @param itemId
 * @param copyId
 */
public record ReserveDTO(
        String itemId,
        String copyId
) {}