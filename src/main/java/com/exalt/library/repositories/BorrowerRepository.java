package com.exalt.library.repositories;

import com.exalt.library.models.users.Borrower;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Borrower documents in MongoDB.
 * Provides standard CRUD operations inherited from MongoRepository
 */
@Repository
public interface BorrowerRepository extends MongoRepository<Borrower, String> {
}
