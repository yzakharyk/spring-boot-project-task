# Demo Application

This is a Spring Boot application for managing records. The application uses PostgreSQL as the database and includes Swagger for API documentation.

## Prerequisites

- Java 21 or higher
- Docker and Docker Compose (if running with Docker)

## Running the Application

You can run the application either locally or using Docker Compose.

### Running Locally

1. **Set up the database**:
   - Ensure you have a PostgreSQL instance running.
   - Update the database connection details in `src/main/resources/application.yml` if necessary.

2. **Build the application**:
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the application**:
   - Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
   - API Base URL: [http://localhost:8080](http://localhost:8080)

### Running with Docker Compose

1. **Build the Docker image**:
   ```bash
   ./mvnw clean package
   ```

2. **Start the application**:
   ```bash
   docker-compose up --build
   ```

3. **Access the application**:
   - Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
   - API Base URL: [http://localhost:8080](http://localhost:8080)

4. **Stop the application**:
   ```bash
   docker-compose down
   ```

## Swagger API Documentation

The application includes Swagger for API documentation. Once the application is running, you can access the Swagger UI at:

- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

This provides a user-friendly interface to explore and test the available API endpoints.

## Troubleshooting

- If you encounter issues with the database connection, ensure the PostgreSQL instance is running and the credentials match those in `application.yml` or `docker-compose.yml`.
- If Swagger UI does not load, ensure the `springdoc-openapi` dependency is correctly included in `pom.xml` and the application is running without errors.
