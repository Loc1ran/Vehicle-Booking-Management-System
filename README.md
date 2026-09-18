# Vehicle Booking Management System

A learning-focused full-stack vehicle booking platform developed from a CLI-based prototype to a complete capstone project. The application enables users to securely reserve vehicles, manage bookings, and track availability in real time. While fully functional, the primary focus of this project is DevOps, emphasizing the end-to-end process of building, testing, and delivering applications to users.

The system is designed using clean architecture principles, strong security best practices, automated database migrations, and a CI/CD pipeline to ensure reliable and repeatable deployments.

---

## Key Highlights
- Secure authentication using **JWT** with Spring Security and bcrypt password hashing
- RESTful API design following **industry best practices**
- **Flyway**-managed database migrations with **foreign key constraints** enforcing referential integrity
- Vehicle image delivery via **Amazon S3 presigned URLs** for time-limited, authenticated access
- Containerized with **Docker** for environment parity
- Automated **CI/CD pipeline** using GitHub Actions
- Deployed to **AWS** with scalable cloud infrastructure
- Comprehensive **unit and integration testing**

---

## Features
- User registration and login
- Vehicle availability tracking
- Booking creation, update, and cancellation
- Conflict prevention for overlapping bookings
- Vehicle image upload to Amazon S3 and retrieval via presigned GET URLs
- Centralized error handling and validation

---

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security (JWT)
- JPA / JDBC
- Flyway

### Frontend
- React
- JavaScript

### Database
- PostgreSQL
- Flyway

### DevOps & Cloud
- Docker
- GitHub Actions
- AWS Elastic Beanstalk
- AWS Amplify
- AWS S3

### Testing
- JUnit
- Mockito
- Testcontainers
- k6 (load testing)

---

## System Architecture
- Layered architecture (Controller → Service → Repository)
- Separation of concerns between business logic and persistence
- Stateless authentication using JWT
- Database versioning with repeatable and versioned migrations

---

## Getting Started

### Prerequisites
- Java 17+
- Docker & Docker Compose

## Load Testing
A k6 load-test script lives in `load-tests/load.js`. It authenticates (registering a test user on first run) and drives staged virtual-user load against authenticated endpoints, with pass/fail thresholds on error rate and p95 latency.

```bash
k6 run -e BASE_URL=http://localhost:8088 load-tests/load.js
```

Override the ramp targets with `RAMP_1` / `RAMP_2` / `RAMP_3` and the credentials with `TEST_USER` / `TEST_PASSWORD`.

## Frontend (Vite + React)
The frontend is built using Vite with React. After navigating to the frontend directory, dependencies can be installed using a Node package manager, and the development server can be started to run the application locally.

### Installation
```bash
git clone https://github.com/Loc1ran/Vehicle-Booking-Management-System.git
cd vehicle-booking-management-system
docker compose up



