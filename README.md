# Transactions Test

This repository implements a **layered architecture**, promoting separation of concerns and maintainability. Below you'll find an overview of the architecture, instructions to run the project, and useful links for testing and code coverage.

---

## Architecture Overview

The project follows a **layered architecture** pattern, structured as:

- **Rest Layer**: Handles HTTP requests/responses (e.g., controllers).
- **Application Service Layer**: Contains software logic, orchestrates operations over domain service layer.
- **Domain Service Layer**: Contains business logic, orchestrates operations, and communicates between layers.
- **Data Access Layer**: Manages persistence operations (e.g., repositories).

This approach ensures that each layer has a distinct responsibility, making the application easier to test and extend.

---

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.4
- **Maven**: 3.8

---

## How to Run the Application

1. **Clone the repository:**
   ```bash
   git clone https://github.com/iliojunior/transactions-test.git
   cd transactions-test
   ```

2. **Build and run the application:**
   ```bash  
   # Using docker with docker-compose
   chmod +X run.sh
   ./run.sh
   ```

3. **Access the application:**
    - The app typically runs on [http://localhost:8080](http://localhost:8080).

---

## Testing & Coverage

- **Jacoco Coverage Report:**  
  After running tests (`mvn clean test`), open the [Coverage Report](target/site/jacoco/index.html)

- **Postman Collection:**  
  Import the provided Postman collection and environment for API testing:  
  [others/postman](others/postman)

---

## Docker build

The docker build is done two steps:
- **Maven build:** Executes `mvn clean package -DskipTests` to package application.
- **Application image:** Copies the generated jar from previous steps and run in `java -jar app.jar`. 

---

## Useful Commands

- **Run tests:**
  ```bash
  mvn clean test
  ```

- **View Jacoco report:**  
  Open `target/site/jacoco/index.html` in your browser after running tests.

---

## Additional Notes

- Make sure you have Java and Maven installed for development mode.
- make sure you have Docker installed supporting compose standard run.
- For API interactions, use the provided Postman collection for accurate request formats.

---

## Links

- [Jacoco Coverage Report](target/site/jacoco/index.html)
- [Postman Collection](others/postman)
