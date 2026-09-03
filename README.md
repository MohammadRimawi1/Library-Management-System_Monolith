# Library Management System

A backend system for managing a library's catalog, borrowers, and reservations — built with Spring Boot and MongoDB.

## What it does

The system supports three types of users — borrowers, librarians, and a single admin account — each with different permissions enforced at both the route level and inside the business logic itself.

- Borrowers can browse the catalog, reserve physical or online items, and return what they've borrowed.
- Librarians manage the catalog: creating books and stories, tracking physical copies, and handling incoming reservations.
- The admin account is seeded on startup and is the only way a borrower gets promoted to librarian — there's no self-service way to register as staff.

Physical items are tracked down to the individual copy. Every physical book has a list of copies, each with its own identity, so the system always knows exactly which physical object a borrower is holding — not just "how many are left." When a copy isn't available, the reservation queues automatically and gets activated the moment that specific copy is returned.

## Design decisions worth knowing about

- **Copies are tracked individually, not as a count.** An earlier version just decremented an integer. That's fragile — it drifts out of sync, and it can't answer "which copy did this person actually take." Copies are now their own objects with their own identity and availability state.
- **Optimistic locking on the item and reservation documents.** Two people trying to reserve the same copy at the same instant is a real race condition, not a hypothetical one. Rather than locking rows or serializing requests, the system uses versioned documents — if two writes collide, the losing one gets a clean 409 instead of silently corrupting data.
- **Database-level uniqueness as a second line of defense.** Application-level duplicate checks (same email, same book) can still race under concurrency. Unique indexes on the actual MongoDB collections catch what the application layer might miss.
- **Every failure mode maps to a specific, correct HTTP status.** A conflict is a 409, a copy that's just been taken by someone else is a 409, bad input is a 400 — there's no case where a client gets a vague 500 for something that should've been a clear, actionable error.

## Tech stack

- **Java 21 / Spring Boot** — REST API, Spring Security, Spring Data MongoDB
- **MongoDB** — schema validation enforced at the database level via `$jsonSchema`, not just in application code
- **JWT** — stateless authentication, role-based authorization
- **JUnit 5 / Mockito** — unit and controller-layer tests
- **Maven**

## Architecture

Controllers handle HTTP, services hold business logic, repositories talk to MongoDB — but a couple of design patterns do real work here rather than existing for their own sake:

- **Factory pattern** for constructing the right item subtype (physical vs. online, book vs. story) from a single creation endpoint.
- **Strategy pattern** for borrowing behavior. A physical item and an online item are borrowed completely differently — one needs a specific copy locked, the other is always available — and the reservation service doesn't need to know which one it's dealing with. It just asks the strategy.

Validation is layered too: request-level validation catches malformed input before it reaches the database, and MongoDB's own schema validation acts as a backstop in case anything gets in through a different path.

## Related work

This system also exists as a microservices rebuild — the same domain split into independent Identity, Catalog, and Reservation services behind an API gateway, containerized with Docker. That's a separate repository, not a branch of this one; the two are meant to be compared as two different approaches to the same problem, not merged into one.

## Known limitations

- No rate limiting on the API yet.
- The default admin credentials are read from environment config but aren't rotated automatically; that'd need to change before this went anywhere near production.

## Getting started

```bash
git clone <repo-url>
cd library-management-system
```

Copy `application-example.yml` to `application.yml` and fill in a MongoDB connection string, a JWT secret, and the admin account's seed credentials.

```bash
mvn clean install
mvn spring-boot:run
```

The API comes up on the configured port, with `/api/auth` handling registration and login.

## Testing

```bash
mvn test
```

Covers controllers (with mocked services and security context), the service layer's business rules, validators, and the factory/strategy implementations directly — including the edge cases, not just the happy path: wrong roles, unavailable copies, duplicate entries, malformed requests.

## License

MIT
