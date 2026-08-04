package com.exalt.library.services.operations;

import com.exalt.library.dto.LibraryItemDTO;
import com.exalt.library.exceptions.notfound.ItemNotFoundException;
import com.exalt.library.models.libraryitems.LibraryItem;

import java.util.List;

/**
 * an interface representing the operations for a Library Item in a library
 * implemented inside LibraryItemServices
 * @author Mohammad Rimawi
 */
public interface LibraryItemOperations {
    /**
     * a method for returning all the library items
     * @return
     */
    List<LibraryItem> getAllItems();

    /**
     * a method for creating a library item from a validated request
     * @param libraryItemDTO
     * @return the created item
     */
    LibraryItem createItem(LibraryItemDTO libraryItemDTO);

    /**
     * a method used to print all the LibraryItems inside the items list
     * implemented inside LibraryItemsServices
     * return a list of items
     */
    List<LibraryItem> printAllItems();

    /**
     *  a method used to find a specific item based on its id
     *  implemented inside LibraryItemsServices
     * @param id
     * @return returns a Book if it exists
     * @throws ItemNotFoundException if the item doesn't exist
     */
    LibraryItem findItemById(String id);

    /**
     * a method for checking if the item exists or not
     * implemented inside LibraryItemsServices
     * @param id - represents the book id
     * @return - returns true or false based if it exists in the list or not
     */
    boolean itemExists(String id);

    /**
     * a method for checking all items if they have titles or not
     * implemented inside LibraryItemsServices
     * @return - true if ALL items have titles, false if AT LEAST one doesn't have
     */
    boolean allItemsHaveTitles();

    /**
     * a method for sorting the items based on alphabetical ascending order
     * implemented inside LibraryItemsServices
     * @return a list of sorted items
     */
    List<LibraryItem> sortItemsByTitle();

    /**
     * a method for checking if all the items are available or not
     * implemented inside LibraryItemsServices
     * @return true if all are available, false if AT LEAST one isn't
     */
    boolean areAllItemsAvailable();

    /**
     * a method for checking how many items exists in the items list
     * implemented inside LibraryItemsServices
     * @return - the size as long
     */
    int getItemCount();
}
