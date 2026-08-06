# Getting Started

Set up the boilerplate on a developer machine and verify it runs.

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| JDK | **21** (toolchain) | App compiles/runs on Java 21. Temurin / JetBrains Runtime 21 recommended for `JAVA_HOME`. Newer daemon JDKs may work with Gradle 9.6+, but prefer 21 (see troubleshooting if you see `What went wrong: 26`). |
| Docker | Recent | Postgres + Testcontainers + Compose |
| Git | Any | Clone this repo |

Optional: Docker Compose v2 (`docker compose`).

### Point your shell at JDK 21

macOS (when multiple JDKs are installed):

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
java -version   # should show 21.x
```

This repo also includes `.java-version` (`21`) for jenv / similar version managers.

## Clone and inspect

```bash
git clone <your-remote-url> boilerplate-springboot
cd boilerplate-springboot
./gradlew --version
```

## Option A — App on host, Postgres in Docker (recommended for development)

```bash
# One shot: Postgres + bootRun
make run-db

# Or step by step
make db-up
make run

# Equivalents
./scripts/manage.sh run --with-db
./scripts/manage.sh help
```

`make` / `manage.sh` prefer JDK 21 for `JAVA_HOME` and wrap Gradle, Compose, format, and test targets.
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
