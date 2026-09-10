package com.exalt.library.models.libraryitems.onlineitems;

/**
 * A class representing the online book that exists in the library as an online object
 * @author Mohammad Rimawi
 */
public class BookOnline extends OnlineItem {
    /**
     * A Default constructor that calls the OnlineItem constructor
     */
    public BookOnline() {
        super();
    }

    /**
     * a method for getting the Book Online Type
     * @return
     */
    @Override
    public String getType() {
        return "BookOnline";
    }

    @Override
    public String toString() {
        return "Online Book{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author=" + getAuthor() +
                ", isAvailable=" + isAvailable() +
                '}';
    }
}
