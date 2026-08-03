package com.exalt.library.models.reservation;

import com.exalt.library.models.users.Borrower;
import com.exalt.library.models.libraryitems.LibraryItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

/**
 * a class representing the reservation for an item
 * it defines who is the borrower going to reserve and what item will he/she reserve
 * it keeps track of the reservation life date
 * @author Mohammad Rimawi
 */
@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id; // Represents the id for a reservation

    @DocumentReference
    private LibraryItem libraryItem; // Defines the item that will be reserved by the borrower

    @DocumentReference
    private Borrower borrower; // Defines the borrower that will reserve a specific item

    private Date startDate; // Defines the start time of the reservation
    private Date availableFrom; // Defines if the item is available so we can count the time of the reservation
    private Date endDate; // Defines when the reservation date ends
    private ReservationStatus status; // Defines the status of the reservation
    private Integer copyNumber; // which specific physical copy this reservation is bound to

    /**
     * A default constructor
     */
    public Reservation() {
        this.status = ReservationStatus.PENDING;
    }

//    ==== GETTERS ====
    /**
     * a method for getting the id of the reservation
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * a method for getting the library item that will be reserved
     * @return
     */
    public LibraryItem getLibraryItem() {
        return libraryItem;
    }

    /**
     * a method for getting the borrower who wants to reserve an item
     * @return
     */
    public Borrower getBorrower() {
        return borrower;
    }

    /**
     * a method for getting the date in which the reservation started
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * a method for getting the date in which the item is currently available
     * if its available, then the user's reservation has a specific time before it ends
     * @return
     */
    public Date getAvailableFrom() {
        return availableFrom;
    }

    /**
     * a method for getting the date in which the reservation ended
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * a method for getting the status of the reservation
     * @return
     */
    public ReservationStatus getStatus() {
        return status;
    }

    /**
     * a method for getting the copy number for the reservation
     * @return
     */
    public Integer getCopyNumber() {
        return copyNumber;
    }

    //    ==== GETTERS ====

//    ==== SETTERS ====
    /**
     * a method for setting the item that will be reserved
     * @param libraryItem
     */
    public void setLibraryItem(LibraryItem libraryItem) {
        this.libraryItem = libraryItem;
    }

    /**
     * a method for setting the borrower who wants to reserve a specific item
     * @param borrower
     */
    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    /**
     * a method for setting the date when the reservation start
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * a method for setting the due date of an active loan, 14 days from the moment it's activated.
     * used to determine when an ACTIVE reservation should flip to EXPIRED if not returned in time.
     * @param activatedAt the moment this reservation became ACTIVE
     */
    public void setDueDate(Date activatedAt) {
        this.availableFrom = activatedAt;
        this.endDate = new Date(activatedAt.getTime() + 14L * 24 * 60 * 60 * 1000); // 14 days
    }

    /**
     * a method for setting the status of a reservation
     * @param status
     */
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    /**
     * a method for setting the copy number for the reservation
     * @param copyNumber
     */
    public void setCopyNumber(Integer copyNumber) {
        this.copyNumber = copyNumber;
    }

    //    ==== SETTERS ====

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", libraryItem=" + libraryItem +
                ", borrower=" + borrower +
                ", startDate=" + startDate +
                ", availableFrom=" + availableFrom +
                ", endDate=" + endDate +
                '}';
    }
}
