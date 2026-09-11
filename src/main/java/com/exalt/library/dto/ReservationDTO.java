package com.exalt.library.dto;

import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.reservation.ReservationStatus;

/**
 * A record representing the Data Transfer Object for a reservation,
 * flattened for the client instead of exposing the full nested item/borrower.
 * @author Mohammad Rimawi
 */
public record ReservationDTO(
        String id,
        String itemId,
        String itemTitle,
        String itemType,
        String copyId,
        String borrowerId,
        String borrowerName,
        ReservationStatus status,
        java.util.Date reservationDate,
        java.util.Date dueDate
) {
    /**
     * builds a ReservationDTO from a Reservation entity
     * @param reservation
     * @return
     */
    public static ReservationDTO from(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getLibraryItem().getId(),
                reservation.getLibraryItem().getTitle(),
                reservation.getLibraryItem().getType(),
                reservation.getCopyId(),
                reservation.getBorrower().getId(),
                reservation.getBorrower().getName(),
                reservation.getStatus(),
                reservation.getStartDate(),
                reservation.getEndDate()
        );
    }
}