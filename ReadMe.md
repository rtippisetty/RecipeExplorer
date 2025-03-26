## Architecture

The project follows a clean architecture based on these key principles:

-   **Separation of Concerns:** Clear division between UI, data, and domain logic.
-   **Single Responsibility:** Each class or module has one specific job.
-   **Testability:** Code is designed to be easy to test.
- **Repository Pattern**: Access data from different sources.
- **Use cases**: Implement the business logic.

The architecture is structured in these layers:

-   **UI Layer:**
    -   Composables: Responsible for rendering the UI and handling user interactions.
    -   ViewModels: Provide data to the composables and handle UI logic.
-   **Domain Layer:**
    -   Use Cases: Define the specific business operations of the application.
    -   Repositories: Define the interfaces for data access.
    -   Models: Represent the business data (Recipe, RecipeDetail).
-   **Data Layer:**
    -   Repositories: Implement the repository interfaces to fetch data.
    -   Network: Handles communication with the API.
    -   Local: Handles communication with the local database.
    -   Mappers: Responsible for mapping between data transfer objects (DTOs) and domain models.