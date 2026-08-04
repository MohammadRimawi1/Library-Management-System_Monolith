//package com.exalt.library.services;
//
//import com.exalt.library.exceptions.notfound.BorrowerNotFoundException;
//import com.exalt.library.models.users.Borrower;
//import com.exalt.library.repositories.BorrowerRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for {@link BorrowerServices}.
// * @author Mohammad Rimawi
// */
//@ExtendWith(MockitoExtension.class)
//class BorrowerServicesTest {
//
//    @Mock
//    private BorrowerRepository borrowerRepository;
//
//    @InjectMocks
//    private BorrowerServices borrowerServices;
//
//    /**
//     * An existing borrower should be returned by ID.
//     */
//    @Test
//    void findBorrowerById_returnsBorrower_whenExists() {
//        Borrower borrower = new Borrower();
//        borrower.setName("Test Borrower");
//        when(borrowerRepository.findById("123")).thenReturn(Optional.of(borrower));
//
//        Borrower result = borrowerServices.findBorrowerById("123");
//
//        assertEquals("Test Borrower", result.getName());
//    }
//
//    /**
//     * A missing borrower ID should throw an exception.
//     */
//    @Test
//    void findBorrowerById_throws_whenNotFound() {
//        when(borrowerRepository.findById("missing")).thenReturn(Optional.empty());
//
//        assertThrows(BorrowerNotFoundException.class,
//                () -> borrowerServices.findBorrowerById("missing"));
//    }
//
//    /**
//     * Existing borrower IDs should return {@code true}.
//     */
//    @Test
//    void borrowerExists_returnsTrue_whenExists() {
//        when(borrowerRepository.existsById("123")).thenReturn(true);
//
//        assertTrue(borrowerServices.borrowerExists("123"));
//    }
//
//    /**
//     * Missing borrower IDs should return {@code false}.
//     */
//    @Test
//    void borrowerExists_returnsFalse_whenNotExists() {
//        when(borrowerRepository.existsById("missing")).thenReturn(false);
//
//        assertFalse(borrowerServices.borrowerExists("missing"));
//    }
//
//    /**
//     * All stored borrowers should be returned.
//     */
//    @Test
//    void getAllBorrowers_returnsFullList() {
//        Borrower b1 = new Borrower();
//        Borrower b2 = new Borrower();
//        when(borrowerRepository.findAll()).thenReturn(List.of(b1, b2));
//
//        List<Borrower> result = borrowerServices.getAllBorrowers();
//
//        assertEquals(2, result.size());
//    }
//
//    /**
//     * The total borrower count should be returned.
//     */
//    @Test
//    void getBorrowerCount_returnsCorrectCount() {
//        when(borrowerRepository.count()).thenReturn(5L);
//
//        assertEquals(5, borrowerServices.getBorrowerCount());
//    }
//}