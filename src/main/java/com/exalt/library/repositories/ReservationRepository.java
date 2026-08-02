package com.exalt.library.repositories;

import com.exalt.library.models.reservation.Reservation;
import com.exalt.library.models.reservation.ReservationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Reservation documents in MongoDB.
 * Provides standard CRUD operations inherited from MongoRepository
 */
@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    /**
     * Retrieves a list of all reservations associated with a specific borrower.
     * @param borrowerId
     * @return
     */
    List<Reservation> findByBorrowerId(String borrowerId);

    /**
     * Retrieves a list of all reservations that match a specific status.
     * @param status
     * @return
     */
    List<Reservation> findByStatus(ReservationStatus status);
}
