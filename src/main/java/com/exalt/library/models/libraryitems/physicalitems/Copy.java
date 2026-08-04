package com.exalt.library.models.libraryitems.physicalitems;

import java.util.UUID;

/**
 * represents a single physical copy of a PhysicalItem.
 * embedded directly inside PhysicalItem's copies list.
 * two borrowers can never end up holding the same copy, and a librarian can tell exactly which physical object a borrower has.
 * @author Mohammad Rimawi
 */
public class Copy {
    private String id; // unique identifier for this specific copy - used for reservation lookups
    private int copyNumber; // the sequential number identifying this specific physical copy
    private boolean available; // whether this specific copy is currently available to reserve

    /**
     * a default constructor
     */
    public Copy() {
    }

    /**
     * constructs a new copy with the given number, available by default
     * @param copyNumber
     */
    public Copy(int copyNumber) {
        this.id = UUID.randomUUID().toString();
        this.copyNumber = copyNumber;
        this.available = true;
    }

//    ==== GETTERS ====
    /**
     * a method for getting the copy ID
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * a method for getting the copy number
     * @return
     */
    public int getCopyNumber() {
        return copyNumber;
    }

    /**
     * a method for getting whether this copy is available
     * @return
     */
    public boolean isAvailable() {
        return available;
    }
//    ==== GETTERS ====

//    ==== SETTERS ====
    /**
     * a method for setting the copy ID
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * a method for setting the copy number
     * @param copyNumber
     */
    public void setCopyNumber(int copyNumber) {
        this.copyNumber = copyNumber;
    }

    /**
     * a method for setting whether this copy is available
     * @param available
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }
//    ==== SETTERS ====

    @Override
    public String toString() {
        return "Copy{" +
                "id='" + id + '\'' +
                ", copyNumber=" + copyNumber +
                ", available=" + available +
                '}';
    }
}