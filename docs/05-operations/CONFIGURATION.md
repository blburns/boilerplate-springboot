# Configuration

## Profiles

Activate with `--spring.profiles.active=` or `SPRING_PROFILES_ACTIVE`.

| Profile | File | Use |
|---------|------|-----|
| *(none / default)* | `application.yml` | Shared defaults |
| `local` | `application-local.yml` | Developer laptop / Compose-linked Postgres |
| `test` | `application-test.yml` | Automated tests |
| `prod` | `application-prod.yml` | Deployed environments |

Profiles stack: default YAML always loads; named profile overlays on top.

## Local profile datasource

Built from discrete env vars (defaults shown):

| Variable | Default |
|----------|---------|
| `DB_HOST` | `localhost` |
| `DB_PORT` | `5432` |
| `DB_NAME` | `boilerplate` |
| `DB_USER` | `boilerplate` |
| `DB_PASSWORD` | `boilerplate` |

JDBC URL shape:

```text
jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
```

## Production profile datasource

Required:

| Variable | Example |
|----------|---------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://db.internal:5432/boilerplate` |
| `SPRING_DATASOURCE_USERNAME` | `boilerplate` |
| `SPRING_DATASOURCE_PASSWORD` | *(secret)* |

Optional shared:

| Variable | Default | Purpose |
|----------|---------|---------|
| `SERVER_PORT` | `8080` | HTTP listen port |
| `SPRING_PROFILES_ACTIVE` | — | Usually `prod` in deployment |

## Flyway / JPA

From `application.yml`:

- Flyway enabled; scripts on `classpath:db/migration`
- `spring.jpa.hibernate.ddl-auto=validate` — Hibernate will **not** create schema
- `open-in-view=false`

## Actuator

Exposed by default: `health`, `info`, `prometheus`.

In `prod`, `management.endpoint.health.show-details` is `never` (from `application-prod.yml`).

## Logging

| Profile | Behavior |
|---------|----------|
| `local` | SQL debug, app DEBUG |
| `test` / `prod` | Quieter (WARN root, INFO app) |

## Optional Compose example

Only if you use the optional `docker-compose.yml` appendix:

```yaml
SPRING_PROFILES_ACTIVE: local
DB_HOST: postgres
DB_PORT: 5432
DB_NAME: boilerplate
DB_USER: boilerplate
DB_PASSWORD: boilerplate
```

Full table: [ENVIRONMENT_VARIABLES](../06-reference/ENVIRONMENT_VARIABLES.md).
