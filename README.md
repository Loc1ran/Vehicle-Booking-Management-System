# Vehicle Booking Management System

A learning-focused full-stack vehicle booking platform developed from a CLI-based prototype to a complete capstone project. The application enables users to securely reserve vehicles, manage bookings, and track availability in real time. While fully functional, the primary focus of this project is DevOps, emphasizing the end-to-end process of building, testing, and delivering applications to users.

The system is designed using clean architecture principles, strong security best practices, automated database migrations, and a CI/CD pipeline to ensure reliable and repeatable deployments.

---

## Key Highlights
- Secure authentication using **JWT** with **role-based access control**
- RESTful API design following **industry best practices**
- **Flyway**-managed database migrations for schema consistency
- Containerized with **Docker** for environment parity
- Automated **CI/CD pipeline** using GitHub Actions
- Deployed to **AWS** with scalable cloud infrastructure
- Comprehensive **unit and integration testing**

---

## Features
- User registration and login
- Role-based permissions (Admin / User)
- Vehicle availability tracking
- Booking creation, update, and cancellation
- Conflict prevention for overlapping bookings
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

## Frontend (Vite + React)
The frontend is built using Vite with React. After navigating to the frontend directory, dependencies can be installed using a Node package manager, and the development server can be started to run the application locally.

### Installation
```bash
git clone https://github.com/Loc1ran/Vehicle-Booking-Management-System.git
cd vehicle-booking-management-system
docker compose up



