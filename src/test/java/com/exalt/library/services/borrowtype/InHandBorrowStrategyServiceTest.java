package com.exalt.library.services.borrowtype;

import com.exalt.library.models.libraryitems.physicalitems.BookPhysical;
import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.reservation.ReservationStatus;
import com.exalt.library.repositories.LibraryItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link InHandBorrowStrategyService}.
 * @author Mohammad Rimawi
 */
class InHandBorrowStrategyServiceTest {

    private LibraryItemRepository libraryItemRepository;
    private InHandBorrowStrategyService strategy;

    /**
     * sets up the strategy with a mocked repository before each test.
     */
    @BeforeEach
    void setUp() {
        libraryItemRepository = mock(LibraryItemRepository.class);
        strategy = new InHandBorrowStrategyService(libraryItemRepository);
    }

    /**
     * activate should decrement the copy count and mark the reservation ACTIVE
     * when the item is available.
     */
    @Test
    void activate_decrementsCopyAndMarksActive_whenItemAvailable() {
        BookPhysical item = new BookPhysical();
        item.setNumOfCopies(2);
        item.setAvailable(true);

        Reservation reservation = new Reservation();
        reservation.setLibraryItem(item);

        Reservation result = strategy.activate(reservation);

        assertEquals(1, item.getNumOfCopies());
        assertTrue(item.isAvailable()); // still copies left, so still available
        assertEquals(ReservationStatus.ACTIVE, result.getStatus());
        verify(libraryItemRepository).save(item);
    }

    /**
     * activate should flip availability off once the last copy is taken.
     */
    @Test
    void activate_marksItemUnavailable_whenLastCopyTaken() {
        BookPhysical item = new BookPhysical();
        item.setNumOfCopies(1);
        item.setAvailable(true);

        Reservation reservation = new Reservation();
        reservation.setLibraryItem(item);

        strategy.activate(reservation);

        assertEquals(0, item.getNumOfCopies());
        assertFalse(item.isAvailable());
    }

    /**
     * activate should not touch the copy count when the item is already unavailable
     * (e.g. it was already held for this borrower via a prior reservation flow).
     */
    @Test
    void activate_doesNotDecrementCopy_whenItemAlreadyUnavailable() {
        BookPhysical item = new BookPhysical();
        item.setNumOfCopies(0);
        item.setAvailable(false);

        Reservation reservation = new Reservation();
        reservation.setLibraryItem(item);

        Reservation result = strategy.activate(reservation);

        assertEquals(0, item.getNumOfCopies());
        assertEquals(ReservationStatus.ACTIVE, result.getStatus());
    }

    /**
     * borrow should delegate to activate and produce the same result.
     */
    @Test
    void borrow_delegatesToActivate() {
        BookPhysical item = new BookPhysical();
        item.setNumOfCopies(3);
        item.setAvailable(true);

        Reservation reservation = new Reservation();
        reservation.setLibraryItem(item);

        Reservation result = strategy.borrow(reservation);

        assertEquals(2, item.getNumOfCopies());
        assertEquals(ReservationStatus.ACTIVE, result.getStatus());
    }

    /**
     * returnItem should increment the copy count and mark the item available again.
     */
    @Test
    void returnItem_incrementsCopyAndMarksAvailable() {
        BookPhysical item = new BookPhysical();
        item.setNumOfCopies(0);
        item.setAvailable(false);

        strategy.returnItem(item);

        assertEquals(1, item.getNumOfCopies());
        assertTrue(item.isAvailable());
    }

    /**
     * holdItem should decrement the copy count directly (used when placing an initial hold).
     */
    @Test
    void holdItem_decrementsCopyCount() {
        BookPhysical item = new BookPhysical();
        item.setNumOfCopies(4);
        item.setAvailable(true);

        strategy.holdItem(item);

        assertEquals(3, item.getNumOfCopies());
        verify(libraryItemRepository).save(item);
    }

    /**
     * holdItem (via decrementCopy) should throw when there are no copies left to take.
     */
    @Test
    void holdItem_throwsIllegalStateException_whenNoCopiesLeft() {
        BookPhysical item = new BookPhysical();
        item.setNumOfCopies(0);

        assertThrows(IllegalStateException.class, () -> strategy.holdItem(item));
    }
}