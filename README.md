# Library Management System — Backend

REST API for managing a library's catalog, borrowers, and reservations. Built with Spring Boot and MongoDB.

Live API: https://library-management-system-monolith-1.onrender.com/api

Hosted on Render's free tier. The first request after a period of inactivity can take up to 30 seconds while the instance restarts.

## Overview

The system supports three roles: borrower, librarian, and a single admin account. Permissions are enforced at both the route level and within the business logic.

Borrowers browse the catalog, reserve physical or online items, and return what they've borrowed. Librarians manage the catalog and handle incoming reservations. The admin account is seeded on startup and is the only way a borrower is promoted to librarian; there is no self-service path to staff access. Admin can also browse all accounts and permanently delete a user, which unwinds their reservation history and releases any copy they hold.

Physical items are tracked at the individual copy level rather than as a count. When a copy is unavailable, a reservation is queued automatically and activated once that copy is returned. Online items are always available and reserve instantly, with a check preventing a borrower from holding more than one active or pending reservation on the same item.

## Design decisions

**Copies are tracked as individual objects, not a count.** A count-based approach drifts out of sync under concurrent writes and can't answer which physical copy a given borrower is holding.

**Optimistic locking on item and reservation documents.** Two borrowers reserving the same copy at the same instant is a real concurrency case, not a hypothetical one. Versioned documents mean a losing write gets a 409 instead of corrupting state.

**Unique indexes at the database level**, in addition to application-level checks, to catch duplicate writes that slip past a race condition in the service layer.

**Every failure path maps to a specific HTTP status.** Conflicts return 409, invalid input returns 400, missing resources return 404. No case falls through to a generic 500.

**API responses go through dedicated DTOs**, not raw MongoDB documents, so the wire format stays stable and independent of internal model structure.

## Stack

- Java 21, Spring Boot, Spring Security, Spring Data MongoDB
- MongoDB Atlas, with schema validation enforced at the database level
- JWT-based stateless authentication
- JUnit 5, Mockito
- Maven
- Docker (multi-stage build)
- Deployed on Render

## Architecture

Controllers handle HTTP, services hold business logic, repositories talk to MongoDB. Two patterns do real work beyond structure:

**Factory pattern** builds the correct item subtype (physical or online, book or story) from a single creation endpoint.

**Strategy pattern** handles borrowing behavior. A physical item requires a specific copy to be locked; an online item is always available. The reservation service doesn't need to know which case it's in.

Validation is layered: request-level checks catch malformed input before it reaches the database, and MongoDB's schema validation acts as a backstop.

## Related work

A microservices rebuild of this same domain exists as a separate repository: Identity, Catalog, and Reservation as independent services behind an API gateway, containerized with Docker. It's intended as a second implementation to compare against this one, not a merge target.

## Known limitations

- No rate limiting yet.
- Admin credentials are set via environment configuration but not rotated automatically.
- Free-tier hosting means cold starts and constrained memory. Not an issue at demo scale.

## Local setup
git clone <repo-url>
cd library-management-system

Copy `application-example.yml` to `application.yml` and set a MongoDB connection string, a JWT secret, and the admin seed credentials.
mvn clean install
mvn spring-boot:run

`/api/auth` handles registration and login.

## Deployment

Built with a multi-stage Dockerfile (Maven build stage, JRE-only runtime stage) and deployed on Render as a Docker web service. All configuration is injected through environment variables; no secrets are committed to the repository. Database is MongoDB Atlas, free M0 tier.

## Testing
mvn test

Covers controllers with mocked services and security context, service-layer business rules, validators, and the factory and strategy implementations directly, including edge cases: wrong roles, unavailable copies, duplicate entries, malformed requests.

## License

MIT
