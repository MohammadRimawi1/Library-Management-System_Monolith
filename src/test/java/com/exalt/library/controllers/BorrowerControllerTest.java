//package com.exalt.library.controllers;
//
//import com.exalt.library.exceptions.notfound.BorrowerNotFoundException;
//import com.exalt.library.exceptions.handler.GlobalExceptionHandler;
//import com.exalt.library.models.users.Borrower;
//import com.exalt.library.services.BorrowerServices;
//import com.exalt.library.services.JwtService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
///**
// * Unit tests for {@link BorrowerController}.
// * @author Mohammad Rimawi
// */
//@WebMvcTest(controllers = BorrowerController.class)
//@Import(GlobalExceptionHandler.class)
//public class BorrowerControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private BorrowerServices borrowerServices;
//
//    @MockitoBean
//    private JwtService jwtService;
//
//    /**
//     * Retrieving all borrowers should return HTTP 200 and the borrower list.
//     */
//    @Test
//    @WithMockUser
//    void findAll_returns200WithList() throws Exception {
//        Borrower borrower = new Borrower();
//        borrower.setName("Test Borrower");
//        when(borrowerServices.getAllBorrowers()).thenReturn(List.of(borrower));
//
//        mockMvc.perform(get("/api/borrowers"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].name").value("Test Borrower"));
//    }
//
//    /**
//     * An existing borrower should be returned with HTTP 200.
//     */
//    @Test
//    @WithMockUser
//    void findById_returns200_whenFound() throws Exception {
//        Borrower borrower = new Borrower();
//        borrower.setName("Test Borrower");
//        when(borrowerServices.findBorrowerById("123")).thenReturn(borrower);
//
//        mockMvc.perform(get("/api/borrowers/123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.name").value("Test Borrower"));
//    }
//
//    /**
//     * A missing borrower should return HTTP 404.
//     */
//    @Test
//    @WithMockUser
//    void findById_returns404_whenNotFound() throws Exception {
//        when(borrowerServices.findBorrowerById("missing"))
//                .thenThrow(new BorrowerNotFoundException("Borrower not found"));
//
//        mockMvc.perform(get("/api/borrowers/missing"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("Not Found"));
//    }
//
//    /**
//     * Retrieving the borrower count should return HTTP 200 and the count.
//     */
//    @Test
//    @WithMockUser
//    void count_returns200WithNumber() throws Exception {
//        when(borrowerServices.getBorrowerCount()).thenReturn(7);
//
//        mockMvc.perform(get("/api/borrowers/count"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(7));
//    }
//}
