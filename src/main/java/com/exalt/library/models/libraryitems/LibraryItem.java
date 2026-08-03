package com.exalt.library.models.libraryitems;

import com.exalt.library.models.Author;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * A class that defines the items in a library
 * there is a story or a book
 * @author Mohammad Rimawi
 */
@Document(collection = "library_items")
public abstract class LibraryItem {

    @Id
    private String id; // Defines the identity number for a book or a story
    private String title; // Defines the title for a book/story
    private Author author; // Defines the author of a specific book/story
    private boolean isAvailable; // Defines if the book is available or not
    private String description; // Defines the description of a book
    private String language; // Defines the language of the book
    private String version; // Defines the edition/version of this item
    private String image; // Defines a URL/path pointing at the item's cover image

    /**
     * A Default constructor
     * sets the availability to true
     */
    public LibraryItem() {
        this.isAvailable = true;
    }


    //    ==== GETTERS ====
    /**
     * a method for getting the id value
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * a method for getting the title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * a method for getting the value of the author
     * @return the author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * a method for getting the value of isAvailable
     * @return if they are available or not
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * a method for getting the description
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * a method for getting the language
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * a method for getting the version/edition of this item
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * a method for getting the cover image URL/path
     * @return the image
     */
    public String getImage() {
        return image;
    }
    //    ==== GETTERS ====

//    ==== SETTERS ====
    /**
     * a method for setting the title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * a method for setting the author
     * @param author
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * a method for setting the value of isAvailable
     * @param available
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * a method for setting the description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * a method for setting the language
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * a method for setting the version/edition of this item
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * a method for setting the cover image URL/path
     * @param image
     */
    public void setImage(String image) {
        this.image = image;
    }
    //    ==== SETTERS ====
}
