# Boilerplate Spring Boot

Multi-module Spring Boot **4.1** / Java **21** application base: REST API, Thymeleaf UI, JPA + Flyway + PostgreSQL, Actuator, OpenAPI, and GitHub Actions CI.

**[Full Documentation →](docs/README.md)**

## Who Is This For?

| Role | Start here |
|------|------------|
| **End user** | [User Guide](docs/02-user-guide/README.md) — Items web UI and API |
| **Administrator** | [Admin Guide](docs/05-operations/ADMIN_GUIDE.md) — config, deploy, monitor |
| **Developer** | [Architecture](docs/03-architecture/ARCHITECTURE.md) — modules and feature recipe |

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

- **JDK 21** for the Gradle daemon and app toolchain (do not run Gradle on JDK 25+)
- Docker (Postgres locally / Testcontainers / Compose)

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS
```

Integration tests use Testcontainers and are skipped automatically when Docker is unavailable. CI runners provide Docker, so those tests run on every pull request.

## Quick Start

```bash
make run-db          # start Postgres + bootRun (JDK 21 preferred)
# or:
./scripts/manage.sh run --with-db
```

- Web UI: http://localhost:8080/items
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health

Control plane: [`scripts/manage.sh`](scripts/manage.sh) · [`Makefile`](Makefile) · [`scripts/README.md`](scripts/README.md)

```bash
make help            # list targets
make test            # unit + integration tests
make verify          # Spotless + test + bootJar (CI-like)
make up              # full Compose stack
make status          # JDK / Docker / health
```

### Full stack with Compose

```bash
make up
# or: docker compose up --build
```

More detail: [Getting Started](docs/01-getting-started/GETTING_STARTED.md) · [First-run checklist](docs/01-getting-started/FIRST_RUN_CHECKLIST.md)

## Common Gradle Tasks

Prefer Make/`manage.sh` above. Direct Gradle:

```bash
./gradlew test              # unit + Testcontainers integration tests
./gradlew spotlessApply     # format Java / Gradle Kotlin DSL
./gradlew :app:bootJar      # fat JAR under app/build/libs/
```

See [Gradle Tasks](docs/06-reference/GRADLE_TASKS.md).

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

Full list: [Environment Variables](docs/06-reference/ENVIRONMENT_VARIABLES.md) · [Configuration](docs/05-operations/CONFIGURATION.md)

## Adding a Feature

1. **Domain** — model + port interface + service in `domain`
2. **Infrastructure** — JPA entity/adapter (+ Flyway migration) in `infrastructure`
3. **API** — controller + request/response DTOs in `api` (optional)
4. **Web** — Thymeleaf controller + template in `web` (optional)
5. Wire a `@Bean` for the domain service in `DomainConfig` if it is a new service

Sample resource: `Item` at `/api/v1/items` and `/items`.

Step-by-step: [Adding a Feature](docs/04-development/ADDING_A_FEATURE.md)

## Security

`SecurityConfig` is intentionally permissive for local demos (API + item pages open). Harden `authorizeHttpRequests` and add JWT/OAuth2 resource-server support before production use.

## CI/CD

- [`.github/workflows/ci.yml`](.github/workflows/ci.yml) — Spotless, tests, `bootJar` on `main` and PRs
- [`.github/workflows/docker.yml`](.github/workflows/docker.yml) — build/push image to `ghcr.io/<owner>/<repo>` on `main` and `v*` tags
- [`.github/dependabot.yml`](.github/dependabot.yml) — weekly Gradle, Actions, and Docker updates

Deploy notes: [Deployment](docs/05-operations/DEPLOYMENT.md)

## License

Use freely as a starting point for internal or open-source apps.
