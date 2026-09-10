package com.exalt.library.models.libraryitems.physicalitems;

/**
 * A class representing the library physical book
 * Each book has a unique id
 * Each book can have a title and be available or not
 * @author Mohammad Rimawi
 */
public class BookPhysical extends PhysicalItem {

    /**
     * A Default constructor that calls the LibraryItem constructor
     */
    public BookPhysical() {
        super();
    }

    /**
     * a method for getting the Book Physical type
     * @return
     */
    @Override
    public String getType() {
        return "BookPhysical";
    }

    @Override
    public String toString() {
        return "Physical Book{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author=" + getAuthor() +
                ", isAvailable=" + isAvailable() + '\'' +
                ", Number of Copies= " + getNumOfCopies() +
                '}';
    }
}
