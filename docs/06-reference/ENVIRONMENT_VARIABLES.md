# Environment Variables

## Server

| Variable | Default | Profiles | Description |
|----------|---------|----------|-------------|
| `SERVER_PORT` | `8080` | all | HTTP port |
| `SPRING_PROFILES_ACTIVE` | — | all | Comma-separated profiles (`local`, `test`, `prod`) |

## Datasource — local profile helpers

Used by `application-local.yml`:

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

## Compose reference values

| Variable | Value in `docker-compose.yml` |
|----------|-------------------------------|
| `SPRING_PROFILES_ACTIVE` | `local` |
| `DB_HOST` | `postgres` |
| `DB_PORT` | `5432` |
| `DB_NAME` | `boilerplate` |
| `DB_USER` | `boilerplate` |
| `DB_PASSWORD` | `boilerplate` |

## Spring Boot standard overrides

Any standard Spring Boot property can be set via env using [relaxed binding](https://docs.spring.io/spring-boot/reference/features/external-config.html), for example:

- `MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info`
- `LOGGING_LEVEL_COM_BOILERPLATE_SPRINGBOOT=DEBUG`
