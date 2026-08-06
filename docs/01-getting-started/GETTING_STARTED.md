# Getting Started

Set up the boilerplate on a developer machine and verify it runs.

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| JDK | 21+ | Temurin recommended |
| Docker | Recent | Postgres + Testcontainers + Compose |
| Git | Any | Clone this repo |

Optional: Docker Compose v2 (`docker compose`).

## Clone and inspect

```bash
git clone <your-remote-url> boilerplate-springboot
cd boilerplate-springboot
./gradlew --version
```

## Option A — App on host, Postgres in Docker (recommended for development)

```bash
# 1. Start database only
docker compose up -d postgres

# 2. Run the application
./gradlew :app:bootRun --args='--spring.profiles.active=local'
```

Default local DB credentials (see `application-local.yml`):

- Host: `localhost:5432`
- Database / user / password: `boilerplate` / `boilerplate` / `boilerplate`

## Option B — Full stack with Compose

Builds the multi-stage image and starts app + Postgres:

```bash
docker compose up --build
```

App listens on **http://localhost:8080**.

## Useful URLs

| URL | What it is |
|-----|------------|
| http://localhost:8080/ | Redirects to Items UI |
| http://localhost:8080/items | Sample Thymeleaf UI |
| http://localhost:8080/swagger-ui.html | OpenAPI / Swagger UI |
| http://localhost:8080/v3/api-docs | OpenAPI JSON |
| http://localhost:8080/actuator/health | Health endpoint |

## Build without running

```bash
./gradlew spotlessApply test :app:bootJar
```

Fat JAR: `app/build/libs/boilerplate-springboot-0.1.0-SNAPSHOT.jar`

## Next

1. [FIRST_RUN_CHECKLIST](FIRST_RUN_CHECKLIST.md)
2. [User Guide](../02-user-guide/README.md) if you want to click through Items
3. [Admin Guide](../05-operations/ADMIN_GUIDE.md) for deployment profiles
