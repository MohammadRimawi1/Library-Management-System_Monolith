package com.exalt.library.dto;

import com.exalt.library.models.Author;
import com.exalt.library.models.libraryitems.LibraryItem;
import com.exalt.library.models.libraryitems.physicalitems.Copy;
import com.exalt.library.models.libraryitems.physicalitems.PhysicalItem;

import java.util.List;

/**
 * A record representing the Data Transfer Object for a library item response.
 * Flattens the polymorphic item hierarchy into one client-friendly shape with
 * a "type" discriminator, and, for physical items, per-copy availability.
 * @author Mohammad Rimawi
 */
public record LibraryItemResponseDTO(
        String id,
        String type,
        String title,
        String description,
        String language,
        String edition,
        String image,
        Author author,
        Integer numOfCopies,
        Integer availableCopies,
        List<CopyDTO> copies
) {
    public static LibraryItemResponseDTO from(LibraryItem item) {
        List<CopyDTO> copies = null;
        Integer numOfCopies = null;
        Integer availableCopies = null;

        if (item instanceof PhysicalItem physicalItem) {
            copies = physicalItem.getCopies().stream().map(CopyDTO::from).toList();
            numOfCopies = copies.size();
            availableCopies = (int) copies.stream()
                    .filter(c -> "AVAILABLE".equals(c.status()))
                    .count();
        }

        return new LibraryItemResponseDTO(
                item.getId(),
                item.getType(),
                item.getTitle(),
                item.getDescription(),
                item.getLanguage(),
                item.getEdition(),
                item.getImage(),
                item.getAuthor(),
                numOfCopies,
                availableCopies,
                copies
        );
    }

    public record CopyDTO(String id, int copyNumber, String status) {
        public static CopyDTO from(Copy copy) {
            return new CopyDTO(copy.getId(), copy.getCopyNumber(), copy.isAvailable() ? "AVAILABLE" : "BORROWED");
        }
    }
}