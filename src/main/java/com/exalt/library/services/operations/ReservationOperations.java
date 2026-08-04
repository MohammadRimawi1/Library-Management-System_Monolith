package com.exalt.library.services.operations;

import com.exalt.library.models.users.Borrower;
import com.exalt.library.exceptions.notfound.BorrowerNotFoundException;
import com.exalt.library.exceptions.notfound.ItemNotFoundException;
import com.exalt.library.exceptions.notfound.ReservationNotFoundException;
import com.exalt.library.models.libraryitems.LibraryItem;
import com.exalt.library.models.reservation.Reservation;

import java.util.List;

/**
 * an interface representing the operations for a Reservation in a library
 * implemented inside ReservationServices
 * @author Mohammad Rimawi
 */
public interface ReservationOperations {
    /**
     * a method for returning all the reservations
     * @return
     */
    List<Reservation> getAllReservations();

    /**
     * a method used to find a specific reservation based on its id
     *  implemented inside ReservationServices
     * @param id
     * @return a reservation
     * @throws ReservationNotFoundException if no reservation was found
     */
    Reservation findReservationById(String id);

    /**
     * Finds an active reservation matching the borrower, the item, and the specific copy
     * implemented inside ReservationServices
     * @param borrowerId
     * @param itemId
     * @param copyId
     * @return the active reservation
     * @throws ReservationNotFoundException if it doesn't exist
     */
    Reservation findActiveReservation(String borrowerId, String itemId, String copyId);

    /**
     * a method for checking if the item exists
     * implemented inside ReservationServices
     * @param itemId
     * @return an item if found
     * @throws ItemNotFoundException
     */
    LibraryItem checkForLibraryItem(String itemId);

    /**
     * a method for checking for the borrower if he exists or not
     * implemented inside ReservationServices
     * @param borrowerId
     * @return borrower if found
     * @throws BorrowerNotFoundException if not found
     */
    Borrower checkForBorrower(String borrowerId);


    /**
     * a method used to let a borrower reserve a specific copy of an item
     * implemented inside ReservationServices
     * @param borrowerId
     * @param itemId
     * @param copyId
     * @return
     */
    Reservation reserve(String borrowerId, String itemId, String copyId);

    /**
     * a method for checking the next pending reservation for a specific copy of an item
     * implemented inside ReservationServices
     * @param item
     * @param copyId
     * @return
     */
    Reservation findNextWaitingReservation(LibraryItem item, String copyId);

    /**
     * a method for handling the expiration of a reservation
     * implemented inside ReservationServices
     * @param reservation
     */
    void checkAndHandleExpiration(Reservation reservation);

    /**
     * a method to close an active reservation so that specific copy becomes available again,
     * and hold it for the next waiting reservation for that same copy (if any)
     * @param reservation
     * @param libraryItem
     */
    void closeReservation(Reservation reservation, LibraryItem libraryItem);

    /**
     * a method which returns a specific borrowed copy and closes its active reservation
     * implemented inside ReservationServices
     * @param borrowerId
     * @param itemId
     * @param copyId
     * @return true if the reservation was closed
     * @throws ReservationNotFoundException if no active reservation is found
     */
    boolean returnItem(String borrowerId, String itemId, String copyId);

    /**
     * A method for retrieving reservations for a specific borrower
     * @param borrowerId
     * @return
     */
    List<Reservation> findReservationsByBorrower(String borrowerId);
}