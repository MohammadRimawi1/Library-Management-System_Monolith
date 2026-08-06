package com.exalt.library.repositories;

import com.exalt.library.models.users.Role;
import com.exalt.library.models.users.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing User documents in MongoDB.
 * Provides standard CRUD operations inherited from MongoRepository
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRole(Role role);
    List<User> findByRole(Role role);
}