//package com.exalt.library.services.borrowtype;
//
//import com.exalt.library.models.libraryitems.onlineitems.BookOnline;
//import com.exalt.library.models.reservation.Reservation;
//import com.exalt.library.models.reservation.ReservationStatus;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertSame;
//
///**
// * Unit tests for {@link OnlineBorrowStrategyService}.
// * @author Mohammad Rimawi
// */
//class OnlineBorrowStrategyServiceTest {
//
//    private final OnlineBorrowStrategyService strategy = new OnlineBorrowStrategyService();
//
//    /**
//     * activate should mark the reservation ACTIVE without any copy/availability logic.
//     */
//    @Test
//    void activate_marksReservationActive() {
//        Reservation reservation = new Reservation();
//        reservation.setLibraryItem(new BookOnline());
//
//        Reservation result = strategy.activate(reservation);
//
//        assertEquals(ReservationStatus.ACTIVE, result.getStatus());
//        assertSame(reservation, result); // same instance, just mutated
//    }
//
//    /**
//     * borrow should delegate to activate and produce the same result.
//     */
//    @Test
//    void borrow_delegatesToActivate() {
//        Reservation reservation = new Reservation();
//        reservation.setLibraryItem(new BookOnline());
//
//        Reservation result = strategy.borrow(reservation);
//
//        assertEquals(ReservationStatus.ACTIVE, result.getStatus());
//    }
//
//    /**
//     * returnItem should be a no-op for online items - it should simply do nothing without throwing.
//     */
//    @Test
//    void returnItem_doesNothing() {
//        BookOnline item = new BookOnline();
//
//        assertDoesNotThrow(() -> strategy.returnItem(item));
//    }
//}