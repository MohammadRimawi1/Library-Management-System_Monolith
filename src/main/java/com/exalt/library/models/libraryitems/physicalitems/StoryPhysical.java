package com.exalt.library.models.libraryitems.physicalitems;

/**
 * A class representing the story that exists in the library as physical object
 * is another type that the library has other than the book
 * @author Mohammad Rimawi
 */
public class StoryPhysical extends PhysicalItem {

    /**
     * A Default constructor that calls the LibraryItem class constructor
     */
    public StoryPhysical() {
        super();
    }

    /**
     * a method for getting the Story Physical Type
     * @return
     */
    @Override
    public String getType() {
        return "StoryPhysical";
    }

    @Override
    public String toString() {
        return "Physical Story{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author=" + getAuthor() +
                ", isAvailable=" + isAvailable() + '\'' +
                ", Number of Copies= " + getNumOfCopies() +
                '}';
    }
}
