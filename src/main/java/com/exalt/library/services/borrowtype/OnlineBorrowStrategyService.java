package com.exalt.library.services.borrowtype;

import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.reservation.ReservationStatus;
import com.exalt.library.services.strategies.BorrowStrategy;
import com.exalt.library.models.libraryitems.LibraryItem;
import org.springframework.stereotype.Component;

/**
 * strategy borrowing online
 * @author Mohammad Rimawi
 */
@Component
public class OnlineBorrowStrategyService implements BorrowStrategy {
    /**
     * a method for activating an existing reservation - online items are
     * always available, so this is just a status flip
     * @param reservation
     * @return the activated reservation
     */
    @Override
    public Reservation activate(Reservation reservation) {
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
     * a method for returning an item
     * @param reservation
     */
    @Override
    public void returnItem(Reservation reservation) {
        // Always available
    }

    @Override
    public boolean isCopyAvailable(LibraryItem item, String copyId) {
        return true; // online items are always available - copies don't apply here
    }
}
