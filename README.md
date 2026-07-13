# Digital Wallet

A microservices-based digital wallet application built with Spring Boot and Spring Cloud. This project demonstrates a scalable, distributed architecture for managing financial transactions and accounts with robust security and data consistency.

## Project Overview

Digital Wallet is a comprehensive financial management system designed with microservices architecture. It provides services for account management, transaction handling, and ledger maintenance with event-driven communication, optimistic locking for concurrent operations, and scheduled reconciliation jobs.

## Architecture

This project uses a **microservices architecture** with the following components:

### Core Services

- **Account Service** - Manages user accounts, profiles, account information, and balance operations with Spring Security
- **Transaction Service** - Handles financial transactions and payment processing with Kafka event listeners
- **Ledger Service** - Maintains transaction ledger, financial records, and scheduled reconciliation jobs
- **Eureka Server** - Service registry for microservice discovery

### Technologies

**Framework & Language:**
- Java 17
- Spring Boot 3.5.4
- Spring Cloud 2025.0.0
- Spring Security

**Key Technologies:**
- **Spring Data JPA** - ORM for database operations
- **Spring Web** - REST API development
- **Spring Security** - Authentication and authorization
- **Netflix Eureka** - Service discovery
- **Apache Kafka** - Event streaming and message broker with multiple consumer support
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
├── account-service/        # Account management microservice with Spring Security
├── transaction-service/    # Transaction processing microservice with Kafka listeners
├── ledger-service/        # Ledger and financial records service with reconciliation jobs
├── eureka-server/         # Service discovery server
└── docker-compose.yml     # Docker orchestration for local development
```

## Key Features

### 🔐 Security
- **Spring Security Integration** - Secured endpoints and authentication
- Account-level security controls

### 💰 Data Consistency & Reliability
- **Optimistic Locking** - Prevents concurrent balance update conflicts with version-based detection
- **Retry Mechanism** - Automatic retry logic (max 3 attempts) for handling concurrent modifications
- **Idempotency Support** - Prevents duplicate transaction processing
- **Concurrent Operation Handling** - Safe handling of simultaneous debit/credit operations

### 📨 Event-Driven Architecture
- **Kafka Event Streaming** - Asynchronous inter-service communication
- **Multiple Consumer Support** - Independent consumers can process the same Kafka event
- **Transaction Event Listeners** - Services react to transaction events in real-time

### 🔄 Ledger Management
- **Scheduled Reconciliation** - Automated ledger reconciliation jobs
- **Transaction History** - Complete audit trail of all account activities
- **Balance Tracking** - Real-time balance updates and verification

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
# Account Service (with Spring Security)
cd account-service
mvn spring-boot:run

# Transaction Service (with Kafka listeners)
cd ../transaction-service
mvn spring-boot:run

# Ledger Service (with reconciliation jobs)
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

### Account Entity Features
- **Optimistic Locking** - @Version field for conflict detection
- **Balance Operations** - Safe debit/credit with retry mechanism
- **Concurrent Safety** - Prevents over-withdrawal and duplicate transactions

## Configuration

Each service includes configuration files for different environments:
- `application.properties` or `application.yml` - Service-specific configuration

Key properties to configure:
- Database connection details (MySQL)
- Eureka server URL
- Kafka broker settings
- Server port
- Spring Security configurations

## Development

### Build

```bash
mvn clean install
```

### Run Tests

```bash
mvn test
```

Tests include:
- Concurrent debit scenarios
- Transaction processing validation
- Ledger reconciliation verification

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
- **Optimistic Locking** - Version-based conflict detection for concurrent updates
- **Retry Pattern** - Automatic retry with bounded limits for transient failures
- **Idempotency** - Ensures safe transaction processing on retries
- **Scheduled Jobs** - Periodic reconciliation and maintenance tasks
- **REST APIs** - Standard HTTP communication between services
- **Database per Service** - Each service manages its own data

## Recent Updates

### Security Enhancements
- Spring Security integration for Account Service

### Reliability Improvements
- Optimistic locking with automatic retry mechanism for concurrent balance updates
- Support for multiple independent Kafka consumers
- Idempotency key handling for transaction safety

### Operational Features
- Scheduled ledger reconciliation jobs
- Enhanced Kafka producer/consumer configuration
- Transaction event listeners across services

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## License

This project is currently unlicensed. See LICENSE file for more details.

## Contact

For questions or support, please reach out to the repository owner: [@anikethate7](https://github.com/anikethate7)

---

**Last Updated:** July 2026
**Latest Commit:** Added Spring Security to Account Service
