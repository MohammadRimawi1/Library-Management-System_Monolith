package com.exalt.library.repositories;

import com.exalt.library.models.libraryitems.LibraryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing LibraryItem documents in MongoDB.
 * Provides standard CRUD operations inherited from MongoRepository
 */
@Repository
public interface LibraryItemRepository extends MongoRepository<LibraryItem, String> {
    /**
     * a method for finding the title, version, and author name.
     * @param title
     * @param edition
     * @param authorName
     * @return
     */
    List<LibraryItem> findByTitleIgnoreCaseAndEditionIgnoreCaseAndAuthor_NameIgnoreCase(String title, String edition, String authorName);
}
