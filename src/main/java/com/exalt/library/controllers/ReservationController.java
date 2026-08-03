package com.exalt.library.controllers;

import com.exalt.library.dto.ReserveDTO;
import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.users.Role;
import com.exalt.library.models.users.User;
import com.exalt.library.services.ReservationServices;
import com.exalt.library.services.UserServices;
import com.exalt.library.util.ApiResponse;
import com.exalt.library.util.SecurityUtils;
import com.exalt.library.validation.ReserveValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * a reservation controller that gets a request from the client, does a specific job then returns the response
 * @author Mohammad Rimawi
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationController  {
    private final ReservationServices reservationServices; // defines the reservation services
    private final UserServices userServices; // defines the reservation services

    /**
     * constructor injection
     * @param reservationServices
     */
    public ReservationController(ReservationServices reservationServices, UserServices userServices) {
        this.reservationServices = reservationServices;
        this.userServices = userServices;
    }

    /**
     * A method for fetching reservations - librarians see all reservations,
     * borrowers only see their own
     * exists on: /api/reservations
     * @return
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll() {
        User currentUser = userServices.findByEmail(SecurityUtils.getCurrentUserEmail());

        if (currentUser.getRole() == Role.LIBRARIAN) {
            return ResponseEntity.ok(ApiResponse.success(200, reservationServices.getAllReservations()));
        }

        List<Reservation> ownReservations = reservationServices.findReservationsByBorrower(currentUser.getBorrower().getId());
        return ResponseEntity.ok(ApiResponse.success(200, ownReservations));
    }

    /**
     * a method for fetching a specific reservation based on a specific id
     * exists on: /api/reservations/{id}
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable String id) {
        Reservation reservation = reservationServices.findReservationById(id);
        reservationServices.checkAndHandleExpiration(reservation);
        return ResponseEntity.ok(ApiResponse.success(200, reservation));
    }

    /**
     * a method for fetching active reservations
     * exists on: /api/reservations/active?borrowerId={id}&itemId={id}&copyNumber={n}
     * @param borrowerId
     * @param itemId
     * @param copyNumber
     * @return
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> findActive(@RequestParam String borrowerId, @RequestParam String itemId, @RequestParam int copyNumber) {
        Reservation reservation = reservationServices.findActiveReservation(borrowerId, itemId, copyNumber);
        return ResponseEntity.ok(ApiResponse.success(200, reservation));
    }

    /**
     * A method for fetching the reservations for a specific borrower
     * @param borrowerId
     * @return
     */
    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<Map<String, Object>> findReservationByBorrower(@PathVariable String borrowerId) {
        List<Reservation> reservations = reservationServices.findReservationsByBorrower(borrowerId);
        return ResponseEntity.ok(ApiResponse.success(200, reservations));
    }

    /**
     * a method for creating a serving request for a reservation
     * exists on: /api/reservations
     * @param reserveDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> reserve(@RequestBody ReserveDTO reserveDTO) {
        ReserveValidator.validate(reserveDTO);

        User currentUser = userServices.findByEmail(SecurityUtils.getCurrentUserEmail());
        Reservation reservation = reservationServices.reserve(currentUser.getBorrower().getId(), reserveDTO.itemId(), reserveDTO.copyNumber());

        return ResponseEntity.status(201).body(ApiResponse.success(201, reservation));
    }

    /**
     * a method for returning an item after being reserved
     * exists on: /api/reservations/return
     * @param reserveDTO
     * @return
     */
    @PostMapping("/return")
    public ResponseEntity<Map<String, Object>> returnItem(@RequestBody ReserveDTO reserveDTO) {
        ReserveValidator.validate(reserveDTO);

        User currentUser = userServices.findByEmail(SecurityUtils.getCurrentUserEmail());
        boolean closed = reservationServices.returnItem(currentUser.getBorrower().getId(), reserveDTO.itemId(), reserveDTO.copyNumber());

        return ResponseEntity.ok(ApiResponse.success(200, Map.of("returned", closed)));
    }

}