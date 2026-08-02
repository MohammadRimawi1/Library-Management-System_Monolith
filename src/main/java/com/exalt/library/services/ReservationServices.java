package com.exalt.library.services;

import com.exalt.library.models.users.Role;
import com.exalt.library.models.users.User;
import com.exalt.library.repositories.ReservationRepository;
import com.exalt.library.services.operations.BorrowerOperations;
import com.exalt.library.services.operations.LibraryItemOperations;
import com.exalt.library.services.operations.ReservationOperations;
import com.exalt.library.services.strategies.BorrowStrategy;
import com.exalt.library.services.factory.BorrowStrategyFactory;
import com.exalt.library.exceptions.ReservationNotFoundException;
import com.exalt.library.models.users.Borrower;
import com.exalt.library.models.libraryitems.LibraryItem;
import com.exalt.library.models.libraryitems.onlineitems.OnlineItem;
import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.reservation.ReservationStatus;
import com.exalt.library.util.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
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

    private final UserServices userServices; // Defines the user services

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
            ReservationRepository reservationRepository,
            UserServices userServices
    ) {
        this.libraryItemOperations = libraryItemOperations;
        this.borrowerOperations = borrowerOperations;
        this.borrowStrategyFactory = borrowStrategyFactory;
        this.reservationRepository = reservationRepository;
        this.userServices = userServices;
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
     * Finds an active reservation matching the borrower and the item ids
     * @param borrowerId
     * @param itemId
     * @return the active reservation
     * @throws ReservationNotFoundException if it doesn't exist
     */
    @Override
    public Reservation findActiveReservation(String borrowerId, String itemId) {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.ACTIVE &&
                        reservation.getBorrower().getId().equals(borrowerId) &&
                        reservation.getLibraryItem().getId().equals(itemId))
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
     * a method used to let a borrower reserve a specific item.
     * if the item is available, the reservation is activated immediately (this replaces the old "loan" path)
     * if not, the reservation is queued as WAITING until the item comes back
     * @param borrowerId
     * @param itemId
     * @return the created reservation
     */
    @Override
    public Reservation reserve(String borrowerId, String itemId) {
        LibraryItem item = checkForLibraryItem(itemId);
        Borrower borrower = checkForBorrower(borrowerId);

        if (item instanceof OnlineItem) {
            throw new IllegalArgumentException("Online items cannot be reserved — they are always available");
        }

        Reservation reservation = new Reservation();
        reservation.setLibraryItem(item);
        reservation.setBorrower(borrower);
        reservation.setStartDate(new Date());

        BorrowStrategy strategy = borrowStrategyFactory.resolve(item);
        if (item.isAvailable()) {
            strategy.activate(reservation);
        }

        reservationRepository.save(reservation);
        return reservation;
    }

    /**
     * a method for checking the next waiting reservation for an item
     * @param item
     * @return
     */
    @Override
    public Reservation findNextWaitingReservation(LibraryItem item) {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .filter(reservation -> reservation.getLibraryItem().getId().equals(item.getId()) &&
                        reservation.getStatus() == ReservationStatus.PENDING)
                .min(Comparator.comparing(Reservation::getStartDate))
                .orElseThrow(() -> new ReservationNotFoundException("No waiting reservation for this item"));
    }

    /**
     * a method for handling the expiration of a reservation
     * @param reservation
     */
    @Override
    public void checkAndHandleExpiration(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.READY
                && reservation.getEndDate() != null
                && new Date().after(reservation.getEndDate())) {

            reservation.setStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(reservation);
        }
    }

    /**
     * a method for canceling a specific reservation and deleting it
     * @param reservation
     * @return
     */
    @Override
    public boolean cancelReservation(Reservation reservation) {
        User currentUser = userServices.findByEmail(SecurityUtils.getCurrentUserEmail());

        if (currentUser.getRole() == Role.BORROWER
                && !reservation.getBorrower().getId().equals(currentUser.getBorrower().getId())) {
            throw new AccessDeniedException("You do not have permission to cancel this reservation");
        }

        if (reservation.getStatus() != ReservationStatus.PENDING && reservation.getStatus() != ReservationStatus.READY) {
            throw new IllegalStateException("Only WAITING or READY reservations can be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
        return true;
    }

    /**
     * a method to close an active reservation so the item becomes available again,
     * and promote the next waiting reservation (if any) into READY status
     * @param reservation
     * @param libraryItem
     */
    @Override
    public void closeReservation(Reservation reservation, LibraryItem libraryItem) {
        reservation.setStatus(ReservationStatus.RETURNED);
        borrowStrategyFactory.resolve(libraryItem).returnItem(libraryItem);
        reservationRepository.save(reservation);

        Reservation next = findNextWaitingReservationOrNull(libraryItem);
        if (next != null) {
            BorrowStrategy strategy = borrowStrategyFactory.resolve(libraryItem);
            strategy.activate(next);
            next.setAvailableFrom(new Date());
            reservationRepository.save(next);
        }
    }

    /**
     * a method which returns a borrowed item and closes its active reservation
     * @param borrowerId
     * @param itemId
     * @return true if the reservation was closed
     * @throws ReservationNotFoundException if no active reservation is found
     */
    @Override
    public boolean returnItem(String borrowerId, String itemId) {
        LibraryItem libraryItem = checkForLibraryItem(itemId);
        Borrower borrower = checkForBorrower(borrowerId);

        Reservation reservation = findActiveReservation(borrower.getId(), libraryItem.getId());
        closeReservation(reservation, libraryItem);

        return true;
    }

    /**
     * a method for a borrower to claim their READY reservation - actually picking up
     * the item that was being held for them
     */
    @Override
    public Reservation claimReservation(Reservation reservation) {
        User currentUser = userServices.findByEmail(SecurityUtils.getCurrentUserEmail());

        if (currentUser.getRole() == Role.BORROWER
                && !reservation.getBorrower().getId().equals(currentUser.getBorrower().getId())) {
            throw new AccessDeniedException("You do not have permission to claim this reservation");
        }

        if (reservation.getStatus() != ReservationStatus.READY) {
            throw new IllegalStateException("Reservation is not ready to be claimed");
        }

        borrowStrategyFactory.resolve(reservation.getLibraryItem()).activate(reservation);
        reservationRepository.save(reservation);

        return reservation;
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
     * a method for retrieving reservations with a specific status
     * @param status
     * @return
     */
    @Override
    public List<Reservation> findReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    /**
     * helper so findNextWaitingReservation's throwing behavior doesn't blow up closeReservation
     * when there's simply nobody waiting
     */
    private Reservation findNextWaitingReservationOrNull(LibraryItem item) {
        try {
            return findNextWaitingReservation(item);
        } catch (ReservationNotFoundException e) {
            return null;
        }
    }
}
