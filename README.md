# Boilerplate Spring Boot

Multi-module Spring Boot **4.1** / Java **21** application base: REST API, Thymeleaf UI, JPA + Flyway + PostgreSQL, Actuator, OpenAPI, and GitHub Actions CI.

**[Full Documentation →](docs/README.md)**

## Who Is This For?

| Role | Start here |
|------|------------|
| **End user** | [User Guide](docs/02-user-guide/README.md) — Items web UI and API |
| **Administrator** | [Admin Guide](docs/05-operations/ADMIN_GUIDE.md) — config, deploy, monitor |
| **Developer** | [Architecture](docs/03-architecture/ARCHITECTURE.md) · [RBAC & Admin](docs/04-development/RBAC_AND_ADMIN.md) |

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

- **JDK 21** for the Gradle daemon and app toolchain
- **PostgreSQL** already installed and reachable (defaults: `localhost:5432`, db/user/password `boilerplate`)
- `psql` for `db-init` / `db-check`

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS
```

Docker is **not** required for development. Compose/image tooling is optional.

## Quick Start

```bash
make db-init         # once — create role + database
make db-check
make run             # bootRun (local profile)
```

Sign in at http://localhost:8080/login  
Default admin: `admin@example.com` / `admin123` (change for production)

- Admin shell: http://localhost:8080/admin
- Items: http://localhost:8080/items
- Swagger UI: http://localhost:8080/swagger-ui.html
- Health: http://localhost:8080/actuator/health

Control plane: [`scripts/manage.sh`](scripts/manage.sh) · [`Makefile`](Makefile) · [`scripts/README.md`](scripts/README.md)

```bash
make help
make test
make verify          # Spotless + test + bootJar
make status
```

More detail: [Getting Started](docs/01-getting-started/GETTING_STARTED.md)

## Common Gradle Tasks

Prefer Make/`manage.sh`. Direct Gradle:

```bash
./gradlew test
./gradlew spotlessApply
./gradlew :app:bootJar
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

Full list: [Environment Variables](docs/06-reference/ENVIRONMENT_VARIABLES.md)

## Adding a Feature

1. **Domain** — model + port + service in `domain`
2. **Infrastructure** — JPA adapter + Flyway migration
3. **API** / **Web** — optional presentation
4. Wire a `@Bean` in `DomainConfig` for new domain services

Step-by-step: [Adding a Feature](docs/04-development/ADDING_A_FEATURE.md)

## Security

Form login + session for the Thymeleaf admin shell. Authorities are permission codes (`items:read`, …) plus `ROLE_{name}`. Default seed user: `admin@example.com` / `admin123`. See [RBAC & Admin](docs/04-development/RBAC_AND_ADMIN.md).

## CI/CD

- [`.github/workflows/ci.yml`](.github/workflows/ci.yml) — Spotless, tests against a CI Postgres **service**, `bootJar`
- [`.github/workflows/docker.yml`](.github/workflows/docker.yml) — optional GHCR image publish
- [`.github/dependabot.yml`](.github/dependabot.yml) — dependency updates

## License

Use freely as a starting point for internal or open-source apps.
