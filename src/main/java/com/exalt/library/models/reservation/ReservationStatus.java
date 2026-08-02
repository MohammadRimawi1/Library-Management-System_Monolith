package com.exalt.library.models.reservation;

/**
 * represents the current state of a reservation
 * @author Mohammad Rimawi
 */
public enum ReservationStatus {
    PENDING, // Means the item isn't available yet
    ACTIVE, // Means the reservation is currently active
    RETURNED, // Means the item was reserved
    EXPIRED, // Means the item reservation is expired
}
