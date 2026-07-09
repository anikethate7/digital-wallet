# Digital Wallet

A microservices-based digital wallet application built with Spring Boot and Spring Cloud. This project demonstrates a scalable, distributed architecture for managing financial transactions and accounts.

## Project Overview

Digital Wallet is a comprehensive financial management system designed with microservices architecture. It provides services for account management, transaction handling, and ledger maintenance with event-driven communication.

## Architecture

This project uses a **microservices architecture** with the following components:

### Core Services

- **Account Service** - Manages user accounts, profiles, and account information
- **Transaction Service** - Handles financial transactions and payment processing
- **Ledger Service** - Maintains transaction ledger and financial records
- **Eureka Server** - Service registry for microservice discovery

### Technologies

**Framework & Language:**
- Java 17
- Spring Boot 3.5.4
- Spring Cloud 2025.0.0

**Key Technologies:**
- **Spring Data JPA** - ORM for database operations
- **Spring Web** - REST API development
- **Netflix Eureka** - Service discovery
- **Apache Kafka** - Event streaming and message broker
- **MySQL** - Primary database
- **Swagger/OpenAPI** - API documentation
- **Lombok** - Boilerplate reduction
- **Maven** - Dependency management

**Infrastructure:**
- Docker & Docker Compose
- Zookeeper (Kafka coordination)
- Kafka 7.5.0

## Project Structure

```
digital-wallet/
├── account-service/        # Account management microservice
├── transaction-service/    # Transaction processing microservice
├── ledger-service/        # Ledger and financial records service
├── eureka-server/         # Service discovery server
└── docker-compose.yml     # Docker orchestration for local development
```

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- MySQL 8.0+ (or use Docker)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/anikethate7/digital-wallet.git
cd digital-wallet
```

### 2. Start Infrastructure with Docker Compose

```bash
docker-compose up -d
```

This will start:
- Zookeeper (port 2181)
- Kafka (port 9092)

### 3. Build the Services

```bash
mvn clean install
```

### 4. Run Individual Services

Each service can be run independently:

```bash
# Account Service
cd account-service
mvn spring-boot:run

# Transaction Service
cd ../transaction-service
mvn spring-boot:run

# Ledger Service
cd ../ledger-service
mvn spring-boot:run

# Eureka Server
cd ../eureka-server
mvn spring-boot:run
```

## API Documentation

Once the services are running, access the Swagger/OpenAPI documentation at:

- Account Service: `http://localhost:<port>/swagger-ui.html`
- Transaction Service: `http://localhost:<port>/swagger-ui.html`
- Ledger Service: `http://localhost:<port>/swagger-ui.html`

## Database

The application uses MySQL for data persistence. Configure the database connection in the `application.properties` or `application.yml` file of each service.

## Features

- **Account Management** - User account creation and management
- **Transaction Processing** - Secure transaction handling and validation
- **Ledger Maintenance** - Comprehensive transaction history and records
- **Service Discovery** - Automatic service registration and discovery via Eureka
- **Event-Driven Architecture** - Asynchronous communication via Kafka
- **API Documentation** - Interactive API docs with Swagger UI

## Configuration

Each service includes configuration files for different environments:
- `application.properties` or `application.yml` - Service-specific configuration

Key properties to configure:
- Database connection details (MySQL)
- Eureka server URL
- Kafka broker settings
- Server port

## Development

### Build

```bash
mvn clean install
```

### Run Tests

```bash
mvn test
```

### Generate JAR

```bash
mvn clean package
```

## Deployment

Services can be containerized and deployed using Docker:

```bash
docker build -t digital-wallet-account-service ./account-service
docker run -d -p 8081:8081 digital-wallet-account-service
```

## Architecture Patterns

- **Microservices** - Independent, loosely coupled services
- **Service Discovery** - Eureka for dynamic service registration
- **Event-Driven** - Kafka for asynchronous inter-service communication
- **REST APIs** - Standard HTTP communication between services
- **Database per Service** - Each service manages its own data

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## License

This project is currently unlicensed. See LICENSE file for more details.

## Contact

For questions or support, please reach out to the repository owner: [@anikethate7](https://github.com/anikethate7)

---

**Last Updated:** July 2026
