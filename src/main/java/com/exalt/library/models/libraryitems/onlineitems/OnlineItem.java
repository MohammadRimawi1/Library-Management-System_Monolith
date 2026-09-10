package com.exalt.library.models.libraryitems.onlineitems;

import com.exalt.library.models.libraryitems.LibraryItem;

/**
 * A class representing the online item that exists in the library as an online object
 * @author Mohammad Rimawi
 */
public class OnlineItem extends LibraryItem {

    /**
     * A Default constructor that calls the LibraryItem constructor
     */
    public OnlineItem() {
        super();
    }

    public String getType() {
        return "";
    }

    @Override
    public String toString() {
        return "Online Item {" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author=" + getAuthor() +
                ", isAvailable=" + isAvailable() +
                '}';
    }
}
