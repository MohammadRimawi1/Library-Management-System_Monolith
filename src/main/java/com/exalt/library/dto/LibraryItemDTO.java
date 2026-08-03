package com.exalt.library.dto;

/**
 * A record representing the Data Transfer Object for a LibraryItem.
 * @param type the type of the library item
 * @param title the title of the library item
 * @param numOfCopies the number of copies (only for physical items)
 * @param description the description of the library item
 * @param language the language of the library item
 * @param version the version of the library item
 * @param image the image of the library item
 * @param author the author information
 * @author Mohammad Rimawi
 */
public record LibraryItemDTO(
        String type,
        String title,
        Integer numOfCopies,
        String description,
        String language,
        String version,
        String image,
        AuthorDTO author
) {}