# Yummy Food Web App (Monolith)

A full-stack food ordering application built as a **monolithic architecture**. The repository contains:

- **Backend**: Spring Boot (Java 21) REST API with PostgreSQL.
- **Frontend**: React + Vite SPA.
- **Docker Compose**: Local containerized setup for backend + database.
- **Kubernetes** manifests for backend and database.

## Table of contents

- [Requirements](#requirements)
- [Repository structure](#repository-structure)
- [Quick start (Docker)](#quick-start-docker)
- [Run locally](#run-locally)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Run on Kubernetes](#run-on-kubernetes)
- [API endpoints](#api-endpoints)
- [Environment variables](#environment-variables)

## Requirements

- **Docker + Docker Compose** (for containers), or
- **Java 21** + **Maven** (for backend locally),
- **Node.js + npm** (for frontend locally).

## Repository structure

```
.
├── backend/              # Spring Boot API
├── frontend/             # React + Vite
├── docker-compose.yml    # Postgres + backend
├── Docker/Init/          # Database initialization SQL
└── k8s/                  # Kubernetes manifests
```

## Quick start (Docker)

1. Build and start services:
   ```bash
   docker compose up --build
   ```
2. Backend is available at:
   ```
   http://localhost:8080
   ```

> **Note:** The frontend service is currently commented out in `docker-compose.yml`.

## Run locally

### Backend

1. Go to the backend directory:
   ```bash
   cd backend
   ```
2. Run the app:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Make sure PostgreSQL is running and environment variables are set.

## Run on Kubernetes

Manifests are located in `k8s/` and include deployments/services for PostgreSQL and the backend.

1. Build the backend image locally (or push your own image to a registry and update the image tag in the manifest):
   ```bash
   minikube start
   minikube docker-env | Invoke-Expression
   docker build -t yummy-backend:1.0.2 ./backend
   docker image ls
   ```
2. Apply the manifests:
   ```bash
   kubectl apply -f k8s/
   kubectl get pods
   ```
3. Port-forward the backend service to access it locally:
   ```bash
   kubectl port-forward svc/yummy-backend 8080:8080
   ```
4. The backend will be available at:
   ```bash
   http://localhost:8080
   ```

## API endpoints

All endpoints are exposed in OpenAPI (swagger) at `http://localhost:8080/swagger-ui`

### Authentication

- `POST /api/auth/register` — Register a new user and return a JWT token.
- `POST /api/auth/login` — Authenticate a user and return a JWT token.

### Restaurants

- `POST /api/restaurants` — Create a restaurant (generic).
- `POST /api/restaurants/me` — Create a restaurant owned by the current user.
- `PUT /api/restaurants/{id}` — Update a restaurant owned by the current user.
- `GET /api/restaurants` — List restaurants (filter by `isActive`, optional `search`).
- `GET /api/restaurants/{id}` — Get a restaurant plus its menu items.
- `GET /api/restaurants/{id}/details` — Get restaurant details.
- `GET /api/restaurants/me` — List restaurants for the current user.

### Menu items

- `POST /api/restaurants/{restaurantId}/menu-items` — Create a menu item for a restaurant owned by the current user.

### Orders

- `POST /api/orders` — Create a new order for the current user.
- `GET /api/orders/me` — List orders for the current user.

## Environment variables

### Backend (Docker Compose)

Configured in `docker-compose.yml`:

- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/yummyfood`
- `SPRING_DATASOURCE_USERNAME=postgres`
- `SPRING_DATASOURCE_PASSWORD=secret`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
- `SPRING_PROFILES_ACTIVE=docker`

### Kubernetes

Configured in `postgres-secret.yaml`:

- `postgres-password: secret`

When running locally, set the same values in your environment.
