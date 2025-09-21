# GEMINI.md

## Settings
* 한글로 대답할 것

## Project Overview

This project, `aoi-api`, is a Spring Boot application designed to generate AI-powered Japanese language quizzes. It leverages the Gemini API to create various problem types, including "find the reading," "find the kanji," and "fill in the reading," based on user-provided text. The application is structured following a standard layered architecture, encompassing controllers, services, repositories, and domain entities.

**Core Technologies:**

*   **Backend:** Java 17, Spring Boot
*   **Build:** Gradle
*   **Web:** Spring Web, Spring WebFlux (for the Gemini API client)
*   **Data:** Spring Data JPA, MySQL
*   **API Documentation:** Swagger/OpenAPI
*   **Testing:** JUnit, MockWebServer, Reactor Test
*   **Utilities:** Lombok

**Architecture:**

*   **`AIQuizController`:** Provides the RESTful endpoint (`/api/v1/create-ai-problems`) for clients to request the creation of AI-generated problems.
*   **`CreateAIProblemsService`:** Implements the primary business logic. It employs a strategy pattern to dynamically select the appropriate problem-generation logic based on the requested `ProblemType`.
*   **`GeminiApiClient`:** A reactive client responsible for making requests to the external Gemini API to fetch the AI-generated content.
*   **`AIProblem` & `User`:** JPA entities that model the application's data, representing the quiz problems and users, respectively.

## Building and Running

This project is managed by Gradle.

*   **Build the project:**
    ```bash
    ./gradlew build
    ```
*   **Run the application:**
    ```bash
    ./gradlew bootRun
    ```
*   **Run tests:**
    ```bash
    ./gradlew test
    ```

## Development Conventions

*   **Code Style:** The project utilizes Lombok to minimize boilerplate code, so developers should use its annotations (e.g., `@Getter`, `@Setter`, `@NoArgsConstructor`).
*   **Testing:** Unit and integration tests are written using JUnit. The `mockwebserver` library is used for testing interactions with the Gemini API, and `reactor-test` is used for testing reactive components.
*   **API-First Approach:** The use of Swagger/OpenAPI suggests an API-first development approach.
*   **Modular Design:** The codebase is organized into distinct packages for controllers, services, repositories, and domain objects, promoting separation of concerns.
