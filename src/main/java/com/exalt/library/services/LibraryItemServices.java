package com.exalt.library.services;

import com.exalt.library.dto.LibraryItemDTO;
import com.exalt.library.exceptions.ItemNotFoundException;
import com.exalt.library.models.Author;
import com.exalt.library.models.libraryitems.LibraryItem;
import com.exalt.library.models.libraryitems.onlineitems.OnlineItem;
import com.exalt.library.models.libraryitems.physicalitems.Copy;
import com.exalt.library.models.libraryitems.physicalitems.PhysicalItem;
import com.exalt.library.repositories.LibraryItemRepository;
import com.exalt.library.services.factory.LibraryItemFactory;
import com.exalt.library.services.operations.LibraryItemOperations;
import com.exalt.library.validation.LibraryItemValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * a class representing the services of the items
 * it implements the Library Item operations
 * @author Mohammad Rimawi
 */
@Service
public class LibraryItemServices implements LibraryItemOperations {

    private final LibraryItemRepository libraryItemRepository; // Defines the libraryItems repository

    /**
     * Constructor injection
     */
    public LibraryItemServices(LibraryItemRepository libraryItemRepository) {
        this.libraryItemRepository = libraryItemRepository;
    }

    /**
     * a method for returning all the library items
     * @return
     */
    @Override
    public List<LibraryItem> getAllItems() {
        return libraryItemRepository.findAll();
    }

    /**
     * a method for creating a library item from a validated request
     * @param libraryItemDTO
     * @return the created item or the updated physical item with new copies
     */
    @Override
    public LibraryItem createItem(LibraryItemDTO libraryItemDTO) {
        LibraryItemValidator.validate(libraryItemDTO);

        LibraryItem item = LibraryItemFactory.create(libraryItemDTO.type());

        validateOnlineItemUniqueness(libraryItemDTO, item);

        if (item instanceof PhysicalItem physicalItem) {
            Optional<PhysicalItem> existingUpdatedItem = handlePhysicalItemCopies(libraryItemDTO, physicalItem);
            if (existingUpdatedItem.isPresent()) {
                return existingUpdatedItem.get();
            }
        }

        item.setTitle(libraryItemDTO.title());
        item.setDescription(libraryItemDTO.description());
        item.setLanguage(libraryItemDTO.language());
        item.setVersion(libraryItemDTO.version());
        item.setImage(libraryItemDTO.image());

        Author author = new Author();
        author.setName(libraryItemDTO.author().name());
        author.setNationality(libraryItemDTO.author().nationality());
        author.setBirthDate(libraryItemDTO.author().birthDate());
        item.setAuthor(author);

        return libraryItemRepository.save(item);
    }

    /**
     * a method used to print all the LibraryItems inside the items repository
     */
    @Override
    public List<LibraryItem> printAllItems() {
        return libraryItemRepository.findAll();
    }

    /**
     *  a method used to find a specific item based on its id
     * @param id
     * @return returns an item if it exists
     * @throws ItemNotFoundException if the item doesn't exist
     */
    @Override
    public LibraryItem findItemById(String id) {
        return libraryItemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item was not found"));
    }

    /**
     * a method for checking if the item exists or not
     * @param id - represents the item id
     * @return - returns true or false based if it exists in the list or not
     */
    @Override
    public boolean itemExists(String id) {
        return libraryItemRepository.existsById(id);
    }

    /**
     * a method for checking all items if they have titles or not
     * @return - true if ALL items have titles, false if AT LEAST one doesn't have
     */
    @Override
    public boolean allItemsHaveTitles() {
        List<LibraryItem> items = libraryItemRepository.findAll();
        return items.stream() // this turns the items into a stream
                .noneMatch(item -> item == null || item.getTitle() == null || item.getTitle().trim().isEmpty());
    }

    /**
     * a method for sorting the items based on alphabetical ascending order
     * @return a list of sorted items
     */
    @Override
    public List<LibraryItem> sortItemsByTitle() {
        List<LibraryItem> items = libraryItemRepository.findAll();
        return items.stream() // this turns the items into a stream
                .sorted(Comparator.comparing(LibraryItem::getTitle)) //create a new sorted stream
                .collect(Collectors.toList()); //terminal operation to accumulate elements from a stream into a mutable container
    }

    /**
     * a method for checking if all the items are available or not
     * @return true if all are available, false if AT LEAST one isn't
     */
    @Override
    public boolean areAllItemsAvailable() {
        List<LibraryItem> items = libraryItemRepository.findAll();
        return items.stream() // this turns the items into a stream
                .allMatch(item -> item.isAvailable()); //check if all elements in the stream matches the condition
    }

    /**
     * a method for checking how many items exists in the items list
     * @return - the size as long
     */
    @Override
    public int getItemCount() {
        return (int) libraryItemRepository.count();
    }

    /**
     * a method for handling physical item copies creation or updating existing physical items
     * @param libraryItemDTO
     * @param physicalItem
     * @return an optional containing the existing physical item if updated, otherwise empty
     */
    private Optional<PhysicalItem> handlePhysicalItemCopies(LibraryItemDTO libraryItemDTO, PhysicalItem physicalItem) {
        var existingPhysicalItem = libraryItemRepository
                .findByTitleIgnoreCaseAndVersionIgnoreCaseAndAuthor_NameIgnoreCase(
                        libraryItemDTO.title(), libraryItemDTO.version(), libraryItemDTO.author().name())
                .stream()
                .filter(PhysicalItem.class::isInstance)
                .map(PhysicalItem.class::cast)
                .findFirst();

        if (existingPhysicalItem.isPresent()) {
            PhysicalItem existingBook = existingPhysicalItem.get();
            addCopiesToExistingBook(existingBook, libraryItemDTO.numOfCopies());
            return Optional.of(libraryItemRepository.save(existingBook));
        }

        initializePhysicalCopies(libraryItemDTO, physicalItem);
        return Optional.empty();
    }

    /**
     * a method for appending new physical copies to an existing physical item sequentially
     * @param existingBook
     * @param copiesToAdd
     */
    private void addCopiesToExistingBook(PhysicalItem existingBook, Integer copiesToAdd) {
        if (copiesToAdd == null || copiesToAdd <= 0) {
            return;
        }

        List<Copy> currentCopies = existingBook.getCopies();
        if (currentCopies == null) {
            currentCopies = new ArrayList<>();
        }

        int startCopyNumber = currentCopies.stream()
                .mapToInt(Copy::getCopyNumber)
                .max()
                .orElse(0) + 1;

        for (int i = 0; i < copiesToAdd; i++) {
            currentCopies.add(new Copy(startCopyNumber + i));
        }

        existingBook.setCopies(currentCopies);
    }

    /**
     * a method for initializing physical copies if the item is a PhysicalItem
     * @param libraryItemDTO
     * @param item
     */
    private void initializePhysicalCopies(LibraryItemDTO libraryItemDTO, LibraryItem item) {
        if (item instanceof PhysicalItem physicalItem && libraryItemDTO.numOfCopies() != null) {
            List<Copy> copies = new ArrayList<>();
            for (int i = 1; i <= libraryItemDTO.numOfCopies(); i++) {
                copies.add(new Copy(i));
            }
            physicalItem.setCopies(copies);
        }
    }

    /**
     * a method for checking duplicate online items before creation
     * @param libraryItemDTO
     * @param item
     * @throws IllegalArgumentException if a matching online item already exists
     */
    private void validateOnlineItemUniqueness(LibraryItemDTO libraryItemDTO, LibraryItem item) {
        if (item instanceof OnlineItem) {
            List<LibraryItem> matches = libraryItemRepository
                    .findByTitleIgnoreCaseAndVersionIgnoreCaseAndAuthor_NameIgnoreCase(
                            libraryItemDTO.title(), libraryItemDTO.version(), libraryItemDTO.author().name());

            boolean duplicateExists = matches.stream()
                    .anyMatch(existing -> existing.getClass().equals(item.getClass()));

            if (duplicateExists) {
                throw new IllegalArgumentException("An online item with this title, version, and author already exists");
            }
        }
    }
}