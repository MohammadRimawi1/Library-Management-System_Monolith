package com.exalt.library.controllers;

import com.exalt.library.dto.ReserveDTO;
import com.exalt.library.exceptions.ReservationNotFoundException;
import com.exalt.library.exceptions.handler.GlobalExceptionHandler;
import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.reservation.ReservationStatus;
import com.exalt.library.models.users.Borrower;
import com.exalt.library.models.users.User;
import com.exalt.library.services.JwtService;
import com.exalt.library.services.ReservationServices;
import com.exalt.library.services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link ReservationController}.
 * @author Mohammad Rimawi
 */
@WebMvcTest(controllers = ReservationController.class)
@Import(GlobalExceptionHandler.class)
class ReservationControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationServices reservationServices;

    @MockitoBean
    private UserServices userServices;

    @MockitoBean
    private JwtService jwtService;

    /**
     * Retrieving all reservations should return HTTP 200 and the reservation list.
     */
    @Test
    @WithMockUser
    void findAll_returns200WithList() throws Exception {
        Reservation reservation = new Reservation();
        when(reservationServices.getAllReservations()).thenReturn(List.of(reservation));

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * An existing reservation should be returned with HTTP 200.
     */
    @Test
    @WithMockUser
    void findById_returns200_whenFound() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.ACTIVE);
        when(reservationServices.findReservationById("123")).thenReturn(reservation);

        mockMvc.perform(get("/api/reservations/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    /**
     * A missing reservation should return HTTP 404.
     */
    @Test
    @WithMockUser
    void findById_returns404_whenNotFound() throws Exception {
        when(reservationServices.findReservationById("missing"))
                .thenThrow(new ReservationNotFoundException("Reservation not found"));

        mockMvc.perform(get("/api/reservations/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Reservation not found"));
    }

    /**
     * Fetching the active reservation for a borrower/item pair should return HTTP 200.
     */
    @Test
    @WithMockUser
    void findActive_returns200_withReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.ACTIVE);
        when(reservationServices.findActiveReservation("borrower1", "item1")).thenReturn(reservation);

        mockMvc.perform(get("/api/reservations/active")
                        .param("borrowerId", "borrower1")
                        .param("itemId", "item1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    /**
     * Fetching reservations by borrower should return HTTP 200 with the list.
     */
    @Test
    @WithMockUser
    void findReservationByBorrower_returns200WithList() throws Exception {
        Reservation reservation = new Reservation();
        when(reservationServices.findReservationsByBorrower("borrower1")).thenReturn(List.of(reservation));

        mockMvc.perform(get("/api/reservations/borrower/borrower1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * Fetching reservations by status should return HTTP 200 with the matching list.
     */
    @Test
    @WithMockUser
    void findReservationsByStatus_returns200WithList() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.WAITING);
        when(reservationServices.findReservationsByStatus(ReservationStatus.WAITING)).thenReturn(List.of(reservation));

        mockMvc.perform(get("/api/reservations/status/waiting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].status").value("WAITING"));
    }

    /**
     * An invalid status path variable should return HTTP 400.
     */
    @Test
    @WithMockUser
    void findReservationsByStatus_returns400_whenStatusInvalid() throws Exception {
        mockMvc.perform(get("/api/reservations/status/not-a-status"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    /**
     * A valid reservation request from an authenticated borrower should return HTTP 201.
     */
    @Test
    @WithMockUser(username = "test@test.com")
    void reserve_returns201_withCreatedReservation() throws Exception {
        ReserveDTO request = new ReserveDTO("507f1f77bcf86cd799439011");

        Borrower borrower = new Borrower();
        User user = new User();
        user.setEmail("test@test.com");
        user.setBorrower(borrower);
        when(userServices.findByEmail("test@test.com")).thenReturn(user);

        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.WAITING);
        when(reservationServices.reserve(any(), any())).thenReturn(reservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("WAITING"));
    }

    /**
     * A reservation request with no itemId should return HTTP 400.
     */
    @Test
    @WithMockUser(username = "test@test.com")
    void reserve_returns400_whenItemIdMissing() throws Exception {
        ReserveDTO request = new ReserveDTO(null);

        mockMvc.perform(post("/api/reservations")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Returning a reserved item should return HTTP 200 with the returned flag.
     */
    @Test
    @WithMockUser(username = "test@test.com")
    void returnItem_returns200_withReturnedFlag() throws Exception {
        ReserveDTO request = new ReserveDTO("507f1f77bcf86cd799439011");

        Borrower borrower = new Borrower();
        User user = new User();
        user.setEmail("test@test.com");
        user.setBorrower(borrower);
        when(userServices.findByEmail("test@test.com")).thenReturn(user);
        when(reservationServices.returnItem(any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/reservations/return")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.returned").value(true));
    }

    /**
     * Claiming a ready reservation should return HTTP 200 with the claimed reservation.
     */
    @Test
    @WithMockUser
    void claim_returns200_withClaimedReservation() throws Exception {
        Reservation reservation = new Reservation();
        Reservation claimed = new Reservation();
        claimed.setStatus(ReservationStatus.ACTIVE);

        when(reservationServices.findReservationById("123")).thenReturn(reservation);
        when(reservationServices.claimReservation(reservation)).thenReturn(claimed);

        mockMvc.perform(post("/api/reservations/123/claim"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    /**
     * Cancelling a reservation should return HTTP 200 with the cancelled flag.
     */
    @Test
    @WithMockUser
    void cancel_returns200_withCancelledFlag() throws Exception {
        Reservation reservation = new Reservation();
        when(reservationServices.findReservationById("123")).thenReturn(reservation);
        when(reservationServices.cancelReservation(reservation)).thenReturn(true);

        mockMvc.perform(delete("/api/reservations/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cancelled").value(true));
    }
}