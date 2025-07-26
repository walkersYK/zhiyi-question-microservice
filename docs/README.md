# Zhiyi Online Judge Microservices System Documentation

English| [中文](README-ch.md)

## Technology Stack Overview

| Category         | Technology/Framework           | Purpose/Usage                          |
|------------------|-------------------------------|----------------------------------------|
| Language         | Java 8                        | Main development language              |
| Build Tool       | Maven                         | Dependency management & build          |
| Main Framework   | Spring Boot 2.6.x             | Microservices foundation               |
| Microservices    | Spring Cloud 2021.x           | Service registration, config, load balancing |
| Service Registry | Nacos                         | Service discovery & registration       |
| Config Center    | Nacos                         | Centralized configuration management   |
| API Gateway      | Spring Cloud Gateway          | Unified API gateway, routing, security |
| Circuit Breaker  | Sentinel                      | Fault tolerance, rate limiting, fallback|
| ORM              | MyBatis-Plus                  | Database operations                    |
| Database         | MySQL                         | Relational database                    |
| Cache/Session    | Redis                         | Distributed cache, session storage     |
| Message Queue    | RabbitMQ                      | Asynchronous service communication     |
| API Docs/Test    | Knife4j (Swagger)             | API documentation & testing            |
| RPC/HTTP Client  | OpenFeign                     | Declarative HTTP calls between services|
| Utility Libs     | Hutool, Apache Commons        | Utility libraries                      |
| Code Sandbox     | Custom + Remote Sandbox API   | Secure code execution & judging        |
| Containerization | Docker, Docker Compose        | Service containerization & orchestration|
| Unit Testing     | JUnit, Spring Boot Test       | Unit & integration testing             |
| Others           | Lombok, EasyExcel, Gson       | Code simplification, Excel, JSON       |

---

## Architecture Description

This project adopts a **Spring Cloud Alibaba microservices architecture** to build a highly cohesive and loosely coupled online judge system. The main modules are as follows:

1. **API Gateway (`zhiyi-gateway`)**
    - Unified entry point for all requests, responsible for routing, authentication (JWT/custom), CORS, rate limiting, and circuit breaking.
    - Uses Nacos for service discovery and dynamic routing to backend services.
    - Aggregates API documentation from all services via Knife4j for easy frontend-backend collaboration.

2. **User Service (`zhiyi-user-service`)**
    - Handles user registration, login, and profile management.
    - Supports distributed session storage with Redis and persistent storage with MySQL.
    - Simplifies database operations with MyBatis-Plus.

3. **Question Service (`zhiyi-question-service`)**
    - Manages CRUD operations for questions, configurations, and test cases.
    - Integrates RabbitMQ for asynchronous, decoupled communication with the judge service.

4. **Judge Service (`zhiyi-judge-service`)**
    - Receives judging requests, invokes custom or remote code sandboxes for secure code execution, and returns results.
    - Supports multiple languages and judging strategies for extensibility.
    - Communicates asynchronously with the question service via RabbitMQ.

5. **Service Client (`zhiyi-service-client`)**
    - Encapsulates OpenFeign clients to simplify inter-service HTTP calls.

6. **Common Modules (`zhiyi-common`, `zhiyi-model`)**
    - Provides shared utilities, constants, exceptions, response wrappers, and data models to improve code reuse.

7. **Service Registry & Config Center**
    - All services register with Nacos for service discovery, dynamic scaling, and hot configuration updates.

8. **Middleware & Infrastructure**
    - Redis for caching and distributed session management.
    - MySQL as the main data store.
    - RabbitMQ for asynchronous messaging between services.
    - Sentinel for circuit breaking, rate limiting, and fallback to ensure system stability.

9. **Containerization & Deployment**
    - All services are Dockerized and orchestrated with Docker Compose for easy local development and production deployment.

---

## Typical Architecture Flow

- Users send requests through the API Gateway, which handles authentication, routing, and rate limiting before forwarding to the appropriate microservice.
- User, question, and judge services are independent microservices, communicating via OpenFeign or message queues.
- The judge service securely executes code via a remote sandbox API and returns the result.
- All services are registered with Nacos for service discovery and health checks.
- Unified API documentation aggregation facilitates frontend-backend collaboration.

---

