# Boilerplate Spring Boot

Multi-module Spring Boot **4.1** / Java **21** application base: REST API, Thymeleaf UI, JPA + Flyway + PostgreSQL, Actuator, OpenAPI, and GitHub Actions CI.

## Modules

| Module | Responsibility |
|--------|----------------|
| `app` | Boot entrypoint, packaging, profiles |
| `api` | REST controllers, DTOs, OpenAPI |
| `web` | Thymeleaf MVC + static assets |
| `domain` | Entities, services, repository ports (no Spring) |
| `infrastructure` | JPA adapters, Flyway, Security, API exception handling |

```text
app → api, web, infrastructure → domain
```

## Prerequisites

- JDK 21+
- Docker (Postgres locally / Testcontainers / Compose)

Integration tests use Testcontainers and are skipped automatically when Docker is unavailable. CI runners provide Docker, so those tests run on every pull request.

## Quick start

```bash
# Start Postgres
docker compose up -d postgres

# Run the app (local profile)
./gradlew :app:bootRun --args='--spring.profiles.active=local'
```

- Web UI: http://localhost:8080/items
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health

### Full stack with Compose

```bash
docker compose up --build
```

## Common Gradle tasks

```bash
./gradlew test              # unit + Testcontainers integration tests
./gradlew spotlessApply     # format Java / Gradle Kotlin DSL
./gradlew :app:bootJar      # fat JAR under app/build/libs/
```

## Configuration

| Variable | Default (local) | Notes |
|----------|-----------------|-------|
| `SERVER_PORT` | `8080` | HTTP port |
| `DB_HOST` | `localhost` | Postgres host |
| `DB_PORT` | `5432` | Postgres port |
| `DB_NAME` | `boilerplate` | Database name |
| `DB_USER` | `boilerplate` | Username |
| `DB_PASSWORD` | `boilerplate` | Password |
| `SPRING_PROFILES_ACTIVE` | — | Use `local`, `test`, or `prod` |
| `SPRING_DATASOURCE_URL` | — | Required in `prod` |
| `SPRING_DATASOURCE_USERNAME` | — | Required in `prod` |
| `SPRING_DATASOURCE_PASSWORD` | — | Required in `prod` |

Profiles live under `app/src/main/resources/application*.yml`.

## Adding a feature

1. **Domain** — model + port interface + service in `domain`
2. **Infrastructure** — JPA entity/adapter (+ Flyway migration) in `infrastructure`
3. **API** — controller + request/response DTOs in `api` (optional)
4. **Web** — Thymeleaf controller + template in `web` (optional)
5. Wire a `@Bean` for the domain service in `DomainConfig` if it is a new service

Sample resource: `Item` at `/api/v1/items` and `/items`.

## Security

`SecurityConfig` is intentionally permissive for local demos (API + item pages open). Harden `authorizeHttpRequests` and add JWT/OAuth2 resource-server support before production use.

## CI/CD

- [`.github/workflows/ci.yml`](.github/workflows/ci.yml) — Spotless, tests, `bootJar` on `main` and PRs
- [`.github/workflows/docker.yml`](.github/workflows/docker.yml) — build/push image to `ghcr.io/<owner>/<repo>` on `main` and `v*` tags
- [`.github/dependabot.yml`](.github/dependabot.yml) — weekly Gradle, Actions, and Docker updates

## License

Use freely as a starting point for internal or open-source apps.
