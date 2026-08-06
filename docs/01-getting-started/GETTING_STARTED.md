# Getting Started

Set up the boilerplate on a developer machine against an **already installed Postgres**.

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| JDK | **21** (toolchain) | Prefer Temurin / JetBrains Runtime 21 for `JAVA_HOME` |
| PostgreSQL | 14+ (16 recommended) | Host service listening on `localhost:5432` (or set `DB_*`) |
| `psql` | Matches server | Used by `db-init` / `db-check` |
| Git | Any | Clone this repo |

Docker is **not** required for development. Compose remains an [optional appendix](#optional-docker-compose).

### Point your shell at JDK 21

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)   # macOS
java -version   # should show 21.x
```

## Clone

```bash
git clone <your-remote-url> boilerplate-springboot
cd boilerplate-springboot
./gradlew --version
```

## Configure Postgres (once)

Defaults (`application-local.yml` / `scripts/manage.sh`):

| Setting | Default |
|---------|---------|
| Host / port | `localhost:5432` |
| Database | `boilerplate` |
| User / password | `boilerplate` / `boilerplate` |

Create role + database (needs a Postgres superuser / peer login):

```bash
make db-init
# or: PGUSER=postgres ./scripts/manage.sh db-init
make db-check
```

Equivalent SQL: [`scripts/sql/init_local_db.sql`](../../scripts/sql/init_local_db.sql).

Copy [`.env.example`](../../.env.example) if you want a local env file for your shell/tooling.

## Run the app

```bash
make run
# or: ./scripts/manage.sh run
```

Useful URLs:

| URL | What it is |
|-----|------------|
| http://localhost:8080/ | Redirects to Items UI |
| http://localhost:8080/items | Sample Thymeleaf UI |
| http://localhost:8080/swagger-ui.html | OpenAPI / Swagger UI |
| http://localhost:8080/actuator/health | Health endpoint |

## Build / test

```bash
make format
make test          # integration tests talk to the same Postgres
make verify        # Spotless + test + bootJar
make jar
```

## Optional: Docker Compose

Only if you explicitly want containers instead of a host Postgres:

```bash
make compose-up
# or: docker compose up --build
```

See root `docker-compose.yml` (marked optional).

## Next

1. [FIRST_RUN_CHECKLIST](FIRST_RUN_CHECKLIST.md)
2. [User Guide](../02-user-guide/README.md)
3. [Admin Guide](../05-operations/ADMIN_GUIDE.md)
