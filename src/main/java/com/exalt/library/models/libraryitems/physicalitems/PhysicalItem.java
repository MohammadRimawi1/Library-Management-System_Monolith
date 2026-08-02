package com.exalt.library.models.libraryitems.physicalitems;

import com.exalt.library.models.libraryitems.LibraryItem;

/**
 * A class representing the physical item that exists in the library as a physical object
 * @author Mohammad Rimawi
 */
public class PhysicalItem extends LibraryItem {
    private int numOfCopies; // Represents how many physical object exists inside the library

    public PhysicalItem() {
        super();
    }

    //    ==== GETTERS ====

    /**
     * a method for getting the number of physical items inside the library
     * @return
     */
    public int getNumOfCopies() {
        return numOfCopies;
    }
//    ==== GETTERS ====

//    ==== SETTERS ====
    /**
     * a method for setting how many physical items exists in the library
     * @param numOfCopies
     */
    public void setNumOfCopies(int numOfCopies) {
        this.numOfCopies = numOfCopies;
    }
//    ==== SETTERS ====

    @Override
    public String toString() {
        return "Physical Item{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author=" + getAuthor() +
                ", isAvailable=" + isAvailable() + '\'' +
                ", Number of Stories= " + numOfCopies +
                '}';
    }
}
