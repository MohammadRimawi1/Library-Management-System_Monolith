package com.exalt.library.services.borrowtype;

import com.exalt.library.exceptions.ItemNotFoundException;
import com.exalt.library.exceptions.ItemUnavailableException;
import com.exalt.library.models.libraryitems.physicalitems.Copy;
import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.reservation.ReservationStatus;
import com.exalt.library.repositories.LibraryItemRepository;
import com.exalt.library.services.strategies.BorrowStrategy;
import com.exalt.library.models.libraryitems.LibraryItem;
import com.exalt.library.models.libraryitems.physicalitems.PhysicalItem;
import org.springframework.stereotype.Component;

/**
 * Strategy borrowing in hand
 * implements both BorrowStrategy and Reservable interfaces
 * @author Mohammad Rimawi
 */
@Component
public class InHandBorrowStrategyService implements BorrowStrategy {
    private final LibraryItemRepository libraryItemRepository; // Defines the library item repository

    /**
     * Constructor injection
     * @param libraryItemRepository
     */
    public InHandBorrowStrategyService(LibraryItemRepository libraryItemRepository) {
        this.libraryItemRepository = libraryItemRepository;
    }

    /**
     * a method for activating an existing reservation - marks it active
     * and decrements the physical copy count
     * @param reservation
     * @return the activated reservation
     */
    @Override
    public Reservation activate(Reservation reservation) {
        PhysicalItem physicalItem = (PhysicalItem) reservation.getLibraryItem();
        Copy copy = findCopy(physicalItem, reservation.getCopyNumber());

        if (!copy.isAvailable()) {
            throw new ItemUnavailableException("Copy #" + reservation.getCopyNumber() + " is not available");
        }

        copy.setAvailable(false);
        physicalItem.setAvailable(hasAnyAvailableCopy(physicalItem));
        libraryItemRepository.save(physicalItem);

        reservation.setStatus(ReservationStatus.ACTIVE);
        return reservation;
    }

    /**
     * The borrowing strategy for a type of borrowing
     * @param reservation
     * @return the activated reservation
     */
    @Override
    public Reservation borrow(Reservation reservation) {
        return activate(reservation);
    }

    /**
     * a method for returning the item
     * @param reservation
     */
    @Override
    public void returnItem(Reservation reservation) {
        PhysicalItem physicalItem = (PhysicalItem) reservation.getLibraryItem();
        Copy copy = findCopy(physicalItem, reservation.getCopyNumber());

        copy.setAvailable(true);
        physicalItem.setAvailable(true);
        libraryItemRepository.save(physicalItem);
    }

    /**
     * a method for checking whether a specific copy is currently available
     * @param item
     * @param copyNumber
     * @return
     */
    @Override
    public boolean isCopyAvailable(LibraryItem item, int copyNumber) {
        PhysicalItem physicalItem = (PhysicalItem) item;
        return findCopy(physicalItem, copyNumber).isAvailable();
    }

    /**
     * finds a specific copy on a physical item by its number
     * @param physicalItem
     * @param copyNumber
     * @return the matching copy
     * @throws ItemNotFoundException if no copy with that number exists
     */
    private Copy findCopy(PhysicalItem physicalItem, int copyNumber) {
        return physicalItem.getCopies().stream()
                .filter(copy -> copy.getCopyNumber() == copyNumber)
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Copy #" + copyNumber + " does not exist for this item"));
    }

    /**
     * a method for checking if there is any available copy
     * @param physicalItem
     * @return
     */
    private boolean hasAnyAvailableCopy(PhysicalItem physicalItem) {
        return physicalItem.getCopies().stream().anyMatch(Copy::isAvailable);
    }
}
