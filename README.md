# Digital Wallet

A microservices-based digital wallet application built with Spring Boot and Spring Cloud. This repository demonstrates a scalable, event-driven architecture for managing user accounts, transactions, and ledger reconciliation using Kafka and Spring Cloud.

Table of Contents
- Project Overview
- Architecture & Components
- Technologies
- Project Structure
- Key Features
- Prerequisites
- Quick Start (Docker)
- Run & Develop Services Locally
- Configuration & Environment Variables
- API Documentation
- Database
- Testing
- Build & Release
- Deployment
- Troubleshooting
- Contributing
- License
- Contact
- Changelog

## Project Overview

Digital Wallet is a modular financial management system composed of independent microservices. Each service owns its data and communicates via Kafka events for eventual consistency. The project emphasizes reliability (idempotency, optimistic locking, retries), observability (API docs), and secure access (Spring Security for the Account Service).

## Architecture & Components

High-level components:
- Account Service — user accounts, authentication, profile, balance management (Spring Security).
- Transaction Service — processes payments, debits/credits, emits transaction events (Kafka producers/consumers).
- Ledger Service — maintains financial ledger, scheduled reconciliation jobs.
- Eureka Server — service discovery and registration.
- Infrastructure — Zookeeper, Kafka, MySQL (per-service databases), Docker Compose for local orchestration.

## Technologies

- Language & Frameworks: Java 17, Spring Boot 3.5.4, Spring Cloud 2025.0.0
- Security: Spring Security
- Messaging: Apache Kafka (7.5.0)
- Database: MySQL 8+
- Persistence: Spring Data JPA
- API Docs: Swagger / OpenAPI
- Build: Maven
- Containerization: Docker, Docker Compose
- Utilities: Lombok

## Project Structure

```
digital-wallet/
├── account-service/        # Account management microservice
├── transaction-service/    # Transaction processing microservice
├── ledger-service/         # Ledger and financial records service
├── eureka-server/          # Service discovery server
└── docker-compose.yml      # Local orchestration
```

## Key Features

- Security: Spring Security enabled for Account Service.
- Event-driven: Kafka-based async communication between services.
- Concurrency & Reliability:
  - Optimistic locking on account balances (@Version).
  - Idempotency keys to prevent duplicate transaction processing.
  - Automatic retry logic for transient failures (configurable limits).
- Ledger & Auditing:
  - Persistent ledger with reconciliation jobs.
  - Full transaction audit trail.
- Scalability:
  - Microservices scale independently.
  - Multiple independent Kafka consumers supported.

## Prerequisites

- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- MySQL 8.0+ (or use the Docker-provided MySQL)

## Quick Start (Docker)

1. Clone the repository:
   git clone https://github.com/anikethate7/digital-wallet.git
   cd digital-wallet

2. Start infrastructure services:
   docker-compose up -d

   This typically starts:
   - Zookeeper (2181)
   - Kafka broker (9092)
   - MySQL (configured in docker-compose)

3. Build the code:
   mvn clean install

4. Run services (examples):
   # from repo root, to run a service:
   cd account-service
   mvn spring-boot:run

## Run & Develop Services Locally

- Each service has its own application configuration (application.yml/properties).
- To run multiple services, ensure each uses distinct ports.
- Use the Eureka server URL in each service's config to register with service discovery.
- Kafka brokers and Zookeeper must be reachable by configured bootstrap servers.

## Configuration & Environment Variables

Common properties to configure for each service:
- spring.datasource.url / username / password — database connection
- spring.kafka.bootstrap-servers — Kafka brokers
- eureka.client.serviceUrl.defaultZone — Eureka server
- server.port — service HTTP port
- security.* — auth settings for Account Service

Provide environment-specific values in application-{profile}.yml or via environment variables (recommended for Docker).

## API Documentation

When running a service locally, Swagger/OpenAPI UI is available (if enabled). Example:
- Account Service: http://localhost:<port>/swagger-ui.html
- Transaction Service: http://localhost:<port>/swagger-ui.html
- Ledger Service: http://localhost:<port>/swagger-ui.html

Check each service's README or application config for exact paths and ports.

## Database

- Each service maintains its own schema (Database per Service pattern).
- For local dev, use the MySQL container started by docker-compose or configure your own DB.
- Ensure proper schema initialization (Flyway/Liquibase if present) or run provided SQL migrations.

## Testing

- Unit tests: mvn test
- Integration tests: configure testcontainers or a local Kafka/MySQL for integration runs.
- Recommended tests:
  - Concurrent debit/credit scenarios
  - Transaction processing and idempotency
  - Ledger reconciliation verification

## Build & Release

- Build JARs:
  mvn clean package
- Example Docker build:
  docker build -t digital-wallet-account-service ./account-service
  docker run -d -p 8081:8081 digital-wallet-account-service

## Deployment

- Containerize each service and deploy to your orchestration platform (Kubernetes, Docker Swarm, ECS).
- Ensure Kafka and Eureka (or an alternative service discovery) are available in your target environment.
- Configure secure network policies for Kafka and DB access.

## Troubleshooting

- Kafka connection errors: verify spring.kafka.bootstrap-servers and ensure broker is reachable.
- Database errors: check datasource URL, credentials, and that the DB is up.
- Service registration issues: confirm Eureka server URL and that services can reach it.

## Contributing

Contributions are welcome. Suggested steps:
1. Fork the repo
2. Create a feature branch: git checkout -b feat/my-feature
3. Commit changes with clear messages
4. Open a Pull Request describing the change and why it's needed

Please include tests for new features or bug fixes.

## License

This project currently has no license specified. If you want a license added (MIT, Apache-2.0, GPLv3, etc.), tell me which one and I can add a LICENSE file and update this README.

## Contact

Repository owner: [@anikethate7](https://github.com/anikethate7)

## Changelog / Recent Updates

- Last updated: 2026-07-13
- Recent: Spring Security added to Account Service (see commit history for details)
