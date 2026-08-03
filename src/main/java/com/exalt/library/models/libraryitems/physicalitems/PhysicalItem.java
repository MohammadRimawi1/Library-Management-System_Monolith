package com.exalt.library.models.libraryitems.physicalitems;

import com.exalt.library.models.libraryitems.LibraryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the physical item that exists in the library as a physical object
 * @author Mohammad Rimawi
 */
public class PhysicalItem extends LibraryItem {
    private List<Copy> copies = new ArrayList<>();

    public PhysicalItem() {
        super();
    }

    //    ==== GETTERS ====

    /**
     * a method for getting the number of physical items inside the library
     * @return
     */
    public int getNumOfCopies() {
        return copies.size();
    }

    /**
     * a method for getting the full list of copies
     * @return
     */
    public List<Copy> getCopies() {
        return copies;
    }
//    ==== GETTERS ====

//    ==== SETTERS ====
    /**
     * a method for replacing the full list of copies directly
     * @param copies
     */
    public void setCopies(List<Copy> copies) {
        this.copies = copies;
    }
//    ==== SETTERS ====

    @Override
    public String toString() {
        return "Physical Item{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author=" + getAuthor() +
                ", isAvailable=" + isAvailable() + '\'' +
                ", Number of Copies= " + getNumOfCopies() +
                '}';
    }
}
