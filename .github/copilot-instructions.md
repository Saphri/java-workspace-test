# Copilot Instructions

## Overview

This project is a Quarkus application that utilizes NATS JetStream for reactive messaging, OIDC for authentication, and Sentry for error logging. It includes REST endpoints, task scheduling, and Docker configurations for various deployment scenarios.

## Key Components

*   **Reactive Messaging:** Uses NATS JetStream for asynchronous communication between services.
*   **Lombok:** The project uses Lombok annotations (such as `@RequiredArgsConstructor`, `@RequiredArgsConstructor` and `@JBossLog`) to reduce boilerplate code. Ensure your IDE has Lombok support enabled.
*   **Immutability:** Prefer using `final` keyword for variables and method parameters, and use `var` for local variable type inference where appropriate to improve code readability and maintain immutability.
*   **Null Safety:** Use `@Nullable` and `@NonNull` annotations from the `javax.annotation` package to indicate nullability of method parameters and return types, enhancing code clarity and reducing potential NullPointerExceptions.
*   **Testing:** Prefer using AssertJ for assertions in tests to improve readability and expressiveness.
*   **Testing:** Prefer using `@QuarkusTest` for tests to leverage Quarkus testing capabilities.
*   **Documentation:** Use Javadoc comments for public methods and classes to ensure clarity and maintainability of the codebase.
*   **Code Style:** Follow standard Java conventions and Quarkus coding guidelines. Use spaces for indentation and ensure consistent formatting across the codebase.
*   **Version Control:** Use meaningful commit messages that describe the changes made. Follow the conventional commit style for clarity.
*   **Dependency Management:** Use Quarkus extensions for managing dependencies and ensure that the `pom.xml` is kept up-to-date with the latest versions of dependencies.
*   **Configuration:** Use `application.properties` for configuration settings, and ensure sensitive information is not hard-coded but managed through environment variables or secure vaults.
*   **Error Handling:** Implement proper error handling in REST endpoints and services. Use Quarkus's built-in exception mappers for consistent error responses.
*   **Security:** Ensure that sensitive endpoints are protected with OIDC and that proper security measures are in place for NATS JetStream communication.
*   **Performance:** Monitor and optimize the performance of the application, especially in areas involving reactive messaging and task scheduling.
*   **Logging:** Use Quarkus's logging capabilities effectively. Ensure that logs are meaningful and provide enough context for debugging.
*   **Code Reviews:** Conduct regular code reviews to maintain code quality and share knowledge among team members.
*   **Continuous Integration:** Ensure that the project is set up with a CI/CD pipeline to automate builds, tests, and deployments.
*   **Documentation:** Maintain up-to-date documentation for the project, including setup instructions, API documentation, and architectural decisions.
*   **Community Guidelines:** Follow the project's contribution guidelines and respect the community standards for collaboration and communication.
*   **Issue Tracking:** Use the issue tracker to report bugs, request features, and track progress on tasks.
*   **Changelog:** Maintain a changelog to document significant changes, new features, and bug fixes in each release.
*   **License:** Ensure that the project adheres to the specified license (e.g., Apache License 2.0) and that all contributions comply with the license terms.
*   **Code Quality Tools:** Use tools like SonarQube or similar for static code analysis to maintain code quality and identify potential issues early.
*   **Dependency Injection:** Use Quarkus's dependency injection features effectively to manage service lifecycles and dependencies.
*   **Reactive Programming:** Leverage Quarkus's reactive programming capabilities to build responsive and resilient applications.
*   **Testing Frameworks:** Use JUnit 5 for unit tests and Quarkus's testing framework for integration tests. Ensure that tests are comprehensive and cover edge cases.
*   **API Versioning:** Implement API versioning strategies to ensure backward compatibility and smooth transitions for clients.
*   **Feature Toggles:** Use feature toggles for experimental features to allow gradual rollouts and testing in production environments.
*   **Performance Monitoring:** Integrate performance monitoring tools to track application performance and identify bottlenecks.
*   **Database Migrations:** Use tools like Flyway for managing database migrations and ensuring schema consistency across environments.
*   **Environment Configuration:** Use profiles in `application.properties` to manage different configurations for development, testing, and production environments.
*   **Build Tools:** Use Maven for building the project and managing dependencies. Ensure that the `pom.xml` is well-structured and follows best practices.
*   **Code Modularity:** Organize code into packages and modules to enhance maintainability and readability. Use clear naming conventions for packages and classes.
*   **API Documentation:** Use OpenAPI annotations to document REST endpoints and ensure that the API is easily discoverable and understandable.
*   **Immutability:** Prefer using immutable data structures where possible to enhance thread safety and reduce side effects in the application.
*   **Reactive Streams:** Use reactive streams effectively to handle backpressure and ensure smooth data flow in reactive components.
*   **Testing Strategies:** Implement a mix of unit tests, integration tests, and end-to-end tests to ensure comprehensive test coverage.
*   **Testing**: Use Quarkus mocking frameworks over Mockito for unit tests to isolate components and test them independently.
*   **Testing**: Use `@TestProfile` for defining specific test profiles to customize the testing environment and configurations.