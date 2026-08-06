# Environment Variables

## Server

| Variable | Default | Profiles | Description |
|----------|---------|----------|-------------|
| `SERVER_PORT` | `8080` | all | HTTP port |
| `SPRING_PROFILES_ACTIVE` | — | all | Comma-separated profiles (`local`, `test`, `prod`) |

## Datasource — local / test helpers

Used by `application-local.yml`, `application-test.yml`, and `scripts/manage.sh`:

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | `localhost` | Postgres hostname |
| `DB_PORT` | `5432` | Postgres port |
| `DB_NAME` | `boilerplate` | Database name |
| `DB_USER` | `boilerplate` | Username |
| `DB_PASSWORD` | `boilerplate` | Password |

## Datasource — production

Required when `prod` is active (`application-prod.yml`):

| Variable | Required | Description |
|----------|----------|-------------|
| `SPRING_DATASOURCE_URL` | yes | Full JDBC URL |
| `SPRING_DATASOURCE_USERNAME` | yes | DB user |
| `SPRING_DATASOURCE_PASSWORD` | yes | DB password |

## Optional Compose overrides

Only if you use the optional `docker-compose.yml` appendix (`DB_HOST=postgres`, etc.).

## Spring Boot standard overrides

Any standard Spring Boot property can be set via env using relaxed binding, for example:

- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info`
- `LOGGING_LEVEL_COM_BOILERPLATE_SPRINGBOOT=DEBUG`
