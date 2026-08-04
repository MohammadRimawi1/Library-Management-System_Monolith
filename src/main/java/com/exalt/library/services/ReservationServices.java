package com.exalt.library.services;

import com.exalt.library.repositories.ReservationRepository;
import com.exalt.library.services.operations.BorrowerOperations;
import com.exalt.library.services.operations.LibraryItemOperations;
import com.exalt.library.services.operations.ReservationOperations;
import com.exalt.library.services.strategies.BorrowStrategy;
import com.exalt.library.services.factory.BorrowStrategyFactory;
import com.exalt.library.exceptions.notfound.ReservationNotFoundException;
import com.exalt.library.models.users.Borrower;
import com.exalt.library.models.libraryitems.LibraryItem;
import com.exalt.library.models.libraryitems.onlineitems.OnlineItem;
import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.reservation.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * a class representing the services for the reservations
 * implements the interface ReservationOperations
 * @author Mohammad Rimawi
 */
@Service
public class ReservationServices implements ReservationOperations {
    private final ReservationRepository reservationRepository; // Defines the reservation repository

    private final LibraryItemOperations libraryItemOperations; // Defines the library item operations
    private final BorrowerOperations borrowerOperations; // Defines the borrower operations
    private final BorrowStrategyFactory borrowStrategyFactory; // Defines the borrower strategy factory

    /**
     * constructor injection
     * @param libraryItemOperations
     * @param borrowerOperations
     * @param borrowStrategyFactory
     */
    public ReservationServices(
            LibraryItemOperations libraryItemOperations,
            BorrowerOperations borrowerOperations,
            BorrowStrategyFactory borrowStrategyFactory,
            ReservationRepository reservationRepository
    ) {
        this.libraryItemOperations = libraryItemOperations;
        this.borrowerOperations = borrowerOperations;
        this.borrowStrategyFactory = borrowStrategyFactory;
        this.reservationRepository = reservationRepository;
    }

    /**
     * a method for returning all the reservations
     * @return
     */
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * a method used to find a specific reservation based on its id
     * @param id
     * @return a reservation
     * @throws ReservationNotFoundException if no reservation was found
     */
    @Override
    public Reservation findReservationById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation was not found"));
    }

    /**
     * Finds an active reservation matching the borrower, the item, and the specific copy
     * @param borrowerId
     * @param itemId
     * @param copyId
     * @return the active reservation
     * @throws ReservationNotFoundException if it doesn't exist
     */
    @Override
    public Reservation findActiveReservation(String borrowerId, String itemId, String copyId) {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.ACTIVE &&
                        reservation.getBorrower().getId().equals(borrowerId) &&
                        reservation.getLibraryItem().getId().equals(itemId) &&
                        copyId.equals(reservation.getCopyId()))
                .findFirst()
                .orElseThrow(() -> new ReservationNotFoundException("Active reservation doesn't exist"));
    }

    /**
     * a method for checking if the library item exists
     * @param itemId
     * @return a library item if found
     */
    @Override
    public LibraryItem checkForLibraryItem(String itemId) {
        return libraryItemOperations.findItemById(itemId);
    }

    /**
     * a method for checking for the borrower if he exists or not
     * @param borrowerId
     * @return borrower if found
     */
    @Override
    public Borrower checkForBorrower(String borrowerId) {
        return borrowerOperations.findBorrowerById(borrowerId);
    }

    /**
     * a method used to let a borrower reserve a specific copy of an item.
     * if that specific copy is available, the reservation is activated immediately
     * if not, the reservation is queued as PENDING until that same copy comes back
     * @param borrowerId
     * @param itemId
     * @param copyId
     * @return the created reservation
     */
    @Override
    public Reservation reserve(String borrowerId, String itemId, String copyId) {
        LibraryItem item = checkForLibraryItem(itemId);
        Borrower borrower = checkForBorrower(borrowerId);

        if (item instanceof OnlineItem) {
            throw new IllegalArgumentException("Online items cannot be reserved — they are always available");
        }

        BorrowStrategy strategy = borrowStrategyFactory.resolve(item);

        Reservation reservation = new Reservation();
        reservation.setLibraryItem(item);
        reservation.setBorrower(borrower);
        reservation.setStartDate(new Date());
        reservation.setCopyId(copyId);

        if (strategy.isCopyAvailable(item, copyId)) {
            strategy.activate(reservation);
            reservation.setDueDate(new Date());
        }

        reservationRepository.save(reservation);
        return reservation;
    }

    /**
     * a method for checking the next pending reservation for a specific copy of an item
     * @param item
     * @param copyId
     * @return
     */
    @Override
    public Reservation findNextWaitingReservation(LibraryItem item, String copyId) {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .filter(reservation -> reservation.getLibraryItem().getId().equals(item.getId()) &&
                        copyId.equals(reservation.getCopyId()) &&
                        reservation.getStatus() == ReservationStatus.PENDING)
                .min(Comparator.comparing(Reservation::getStartDate))
                .orElseThrow(() -> new ReservationNotFoundException("No pending reservation for this copy"));
    }

    /**
     * a method for handling the expiration of a reservation
     * @param reservation
     */
    @Override
    public void checkAndHandleExpiration(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.ACTIVE
                && reservation.getEndDate() != null
                && new Date().after(reservation.getEndDate())) {

            reservation.setStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(reservation);
        }
    }

    /**
     * a method to close an active reservation so that specific copy becomes available again,
     * and promote the next pending reservation for that same copy (if any) straight to ACTIVE
     * @param reservation
     * @param libraryItem
     */
    @Override
    public void closeReservation(Reservation reservation, LibraryItem libraryItem) {
        reservation.setStatus(ReservationStatus.RETURNED);
        borrowStrategyFactory.resolve(libraryItem).returnItem(reservation);
        reservationRepository.save(reservation);

        Reservation next = findNextWaitingReservationOrNull(libraryItem, reservation.getCopyId());
        if (next != null) {
            BorrowStrategy strategy = borrowStrategyFactory.resolve(libraryItem);
            strategy.activate(next);
            next.setDueDate(new Date());
            reservationRepository.save(next);
        }
    }

    /**
     * a method which returns a specific borrowed copy and closes its active reservation
     * @param borrowerId
     * @param itemId
     * @param copyId
     * @return true if the reservation was closed
     * @throws ReservationNotFoundException if no active reservation is found
     */
    @Override
    public boolean returnItem(String borrowerId, String itemId, String copyId) {
        LibraryItem libraryItem = checkForLibraryItem(itemId);
        Borrower borrower = checkForBorrower(borrowerId);

        Reservation reservation = findActiveReservation(borrower.getId(), libraryItem.getId(), copyId);
        closeReservation(reservation, libraryItem);

        return true;
    }

    /**
     * A method for retrieving reservations for a specific borrower
     * @param borrowerId
     * @return
     */
    @Override
    public List<Reservation> findReservationsByBorrower(String borrowerId) {
        return reservationRepository.findByBorrowerId(borrowerId);
    }

    /**
     * helper so findNextWaitingReservation's throwing behavior doesn't blow up closeReservation
     * when there's simply nobody waiting for that specific copy
     */
    private Reservation findNextWaitingReservationOrNull(LibraryItem item, String copyId) {
        if (copyId == null) return null;
        try {
            return findNextWaitingReservation(item, copyId);
        } catch (ReservationNotFoundException e) {
            return null;
        }
    }
}