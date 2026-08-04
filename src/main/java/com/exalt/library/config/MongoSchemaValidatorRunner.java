package com.exalt.library.config;

import com.exalt.library.models.libraryitems.LibraryItem;
import com.exalt.library.models.users.User;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * applies MongoDB $jsonSchema validation to collections at application startup
 * this is enforced by the database itself
 * @author Mohammad Rimawi
 */
@Component
public class MongoSchemaValidatorRunner implements CommandLineRunner {
    private final MongoTemplate mongoTemplate;

    /**
     * Constructs the runner with the necessary {@link MongoTemplate}.
     * @param mongoTemplate Spring's template for interacting with MongoDB
     */
    public MongoSchemaValidatorRunner(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Callback method executed automatically by Spring Boot after the application context loads.
     * Triggers the initialization/modification of database schema rules.
     * @param args incoming command line arguments
     */
    @Override
    public void run(String... args) {
        applyBorrowerValidation();
        applyLibraryItemValidation();
        applyReservationValidation();
        applyUserIndexes();
        applyLibraryItemIndexes();
    }

    /**
     * A method for applying the borrower validation on the schema level
     */
    private void applyBorrowerValidation() {
        MongoDatabase db = mongoTemplate.getDb();

        Document schema = new Document("$jsonSchema", new Document()
                .append("bsonType", "object")
                .append("required", java.util.List.of("name"))
                .append("properties", new Document()
                        .append("name", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 2)
                                .append("maxLength", 100)
                                .append("description", "must be a string between 2 and 100 characters"))
                )
        );

        applyValidator(db, "borrowers", schema);
    }

    /**
     * A method for applying the LibraryItem validation on the schema level
     */
    private void applyLibraryItemValidation() {
        MongoDatabase db = mongoTemplate.getDb();

        Document schema = new Document("$jsonSchema", new Document()
                .append("bsonType", "object")
                .append("required", List.of("title", "language"))
                .append("properties", new Document()
                        .append("title", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 2)
                                .append("maxLength", 150)
                                .append("description", "must be a string between 2 and 150 characters"))
                        .append("language", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 2)
                                .append("maxLength", 50)
                                .append("description", "must be a string between 2 and 50 characters"))
                        .append("description", new Document()
                                .append("bsonType", "string")
                                .append("maxLength", 2000)
                                .append("description", "must not exceed 2000 characters"))
                )
        );

        applyValidator(db, "library_items", schema);
    }

    /**
     * A method for applying the reservation validation on the schema level
     */
    private void applyReservationValidation() {
        MongoDatabase db = mongoTemplate.getDb();

        Document schema = new Document("$jsonSchema", new Document()
                .append("bsonType", "object")
                .append("required", List.of("status"))
                .append("properties", new Document()
                        .append("status", new Document()
                                .append("bsonType", "string")
                                .append("enum", List.of("PENDING", "ACTIVE", "RETURNED", "EXPIRED"))
                                .append("description", "must be one of the defined reservation statuses"))
                )
        );

        applyValidator(db, "reservations", schema);
    }

    /**
     * applies a $jsonSchema validator to the given collection
     * strict + error means every insert/update is checked, and invalid documents are rejected outright
     * @param db
     * @param collectionName
     * @param schema
     */
    private void applyValidator(MongoDatabase db, String collectionName, Document schema) {
        Document command = new Document("collMod", collectionName)
                .append("validator", schema)
                .append("validationLevel", "strict")
                .append("validationAction", "error");

        db.runCommand(command);
    }

    /**
     * A method for ensuring a unique index exists on User.email, so the database itself
     * rejects concurrent duplicate registrations instead of relying only on an app-level check
     */
    private void applyUserIndexes() {
        mongoTemplate.indexOps(User.class)
                .createIndex(new Index().on("email", org.springframework.data.domain.Sort.Direction.ASC).unique());
    }

    /**
     * A method for ensuring a unique compound index on title + edition + author.name,
     * so the database itself rejects a concurrent duplicate item creation instead of
     * relying only on the app-level check in LibraryItemServices
     */
    private void applyLibraryItemIndexes() {
        mongoTemplate.indexOps(LibraryItem.class)
                .createIndex(new Index()
                        .on("title", org.springframework.data.domain.Sort.Direction.ASC)
                        .on("edition", org.springframework.data.domain.Sort.Direction.ASC)
                        .on("author.name", org.springframework.data.domain.Sort.Direction.ASC)
                        .unique()
                        .collation(Collation.of("en").strength(Collation.ComparisonLevel.secondary())));
    }
}
