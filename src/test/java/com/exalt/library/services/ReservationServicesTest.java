//package com.exalt.library.services;
//
//import com.exalt.library.exceptions.ReservationNotFoundException;
//import com.exalt.library.models.libraryitems.LibraryItem;
//import com.exalt.library.models.libraryitems.physicalitems.PhysicalItem;
//import com.exalt.library.models.reservation.Reservation;
//import com.exalt.library.models.reservation.ReservationStatus;
//import com.exalt.library.models.users.Borrower;
//import com.exalt.library.models.users.Role;
//import com.exalt.library.models.users.User;
//import com.exalt.library.repositories.ReservationRepository;
//import com.exalt.library.services.factory.BorrowStrategyFactory;
//import com.exalt.library.services.operations.BorrowerOperations;
//import com.exalt.library.services.operations.LibraryItemOperations;
//import com.exalt.library.services.strategies.BorrowStrategy;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for {@link ReservationServices}.
// * @author Mohammad Rimawi
// */
//@ExtendWith(MockitoExtension.class)
//class ReservationServicesTest {
//
//    @Mock private ReservationRepository reservationRepository;
//    @Mock private LibraryItemOperations libraryItemOperations;
//    @Mock private BorrowerOperations borrowerOperations;
//    @Mock private BorrowStrategyFactory borrowStrategyFactory;
//    @Mock private BorrowStrategy borrowStrategy;
//    @Mock private UserServices userServices;
//
//    @InjectMocks
//    private ReservationServices reservationServices;
//
//    /**
//     * Clears the security context after each test.
//     */
//    @AfterEach
//    void clearSecurityContext() {
//        SecurityContextHolder.clearContext();
//    }
//
//    /**
//     * Available items should activate the reservation immediately.
//     */
//    @Test
//    void reserve_activatesImmediately_whenItemAvailable() {
//        PhysicalItem item = mock(PhysicalItem.class);
//        when(item.isAvailable()).thenReturn(true);
//        Borrower borrower = new Borrower();
//        borrower.setName("Test Borrower");
//
//        when(libraryItemOperations.findItemById("item-1")).thenReturn(item);
//        when(borrowerOperations.findBorrowerById("borrower-1")).thenReturn(borrower);
//        when(borrowStrategyFactory.resolve(item)).thenReturn(borrowStrategy);
//
//        Reservation result = reservationServices.reserve("borrower-1", "item-1");
//
//        assertEquals(borrower, result.getBorrower());
//        assertEquals(item, result.getLibraryItem());
//        verify(borrowStrategy).activate(result);
//        verify(reservationRepository).save(result);
//    }
//
//    /**
//     * Unavailable items should leave the reservation waiting.
//     */
//    @Test
//    void reserve_staysWaiting_whenItemUnavailable() {
//        PhysicalItem item = mock(PhysicalItem.class);
//        when(item.isAvailable()).thenReturn(false);
//        Borrower borrower = new Borrower();
//
//        when(libraryItemOperations.findItemById("item-1")).thenReturn(item);
//        when(borrowerOperations.findBorrowerById("borrower-1")).thenReturn(borrower);
//
//        Reservation result = reservationServices.reserve("borrower-1", "item-1");
//
//        assertEquals(ReservationStatus.PENDING, result.getStatus());
//        verify(borrowStrategy, never()).activate(any());
//        verify(reservationRepository).save(result);
//    }
//
//    /**
//     * Looking up a missing reservation should throw an exception.
//     */
//    @Test
//    void findReservationById_throws_whenNotFound() {
//        when(reservationRepository.findById("missing")).thenReturn(java.util.Optional.empty());
//
//        assertThrows(ReservationNotFoundException.class,
//                () -> reservationServices.findReservationById("missing"));
//    }
//
//    /**
//     * Reservation owners should be able to claim ready reservations.
//     */
//    @Test
//    void claimReservation_succeeds_whenCallerOwnsReservation() {
//        Borrower borrower = new Borrower();
//        setField(borrower, "id", "borrower-1");
//
//        Reservation reservation = new Reservation();
//        reservation.setStatus(ReservationStatus.READY);
//        reservation.setBorrower(borrower);
//        reservation.setLibraryItem(mock(LibraryItem.class));
//
//        loginAs("borrower1@test.com");
//        User caller = new User();
//        caller.setRole(Role.BORROWER);
//        caller.setBorrower(borrower);
//        when(userServices.findByEmail("borrower1@test.com")).thenReturn(caller);
//        when(borrowStrategyFactory.resolve(any())).thenReturn(borrowStrategy);
//
//        Reservation result = reservationServices.claimReservation(reservation);
//
//        verify(borrowStrategy).activate(reservation);
//        assertEquals(reservation, result);
//    }
//
//    /**
//     * Users should not be able to claim another borrower's reservation.
//     */
//    @Test
//    void claimReservation_throwsAccessDenied_whenCallerDoesNotOwnReservation() {
//        Borrower owner = new Borrower();
//        setField(owner, "id", "borrower-1");
//        Borrower caller = new Borrower();
//        setField(caller, "id", "borrower-2");
//
//        Reservation reservation = new Reservation();
//        reservation.setStatus(ReservationStatus.READY);
//        reservation.setBorrower(owner);
//
//        loginAs("borrower2@test.com");
//        User callerUser = new User();
//        callerUser.setRole(Role.BORROWER);
//        callerUser.setBorrower(caller);
//        when(userServices.findByEmail("borrower2@test.com")).thenReturn(callerUser);
//
//        assertThrows(AccessDeniedException.class,
//                () -> reservationServices.claimReservation(reservation));
//    }
//
//    /**
//     * Authenticates a user for the current test.
//     *
//     * @param email the authenticated user's email
//     */
//    private void loginAs(String email) {
//        var auth = new UsernamePasswordAuthenticationToken(email, null, java.util.List.of());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }
//
//    /**
//     * Sets the value of a private field using reflection.
//     *
//     * @param target the target object
//     * @param fieldName the field name
//     * @param value the value to assign
//     */
//    private void setField(Object target, String fieldName, Object value) {
//        try {
//            var field = target.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true);
//            field.set(target, value);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}