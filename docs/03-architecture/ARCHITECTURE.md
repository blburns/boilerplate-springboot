# Architecture

## Stack

| Layer | Choice |
|-------|--------|
| Language | Java 21 |
| Framework | Spring Boot 4.1 |
| Build | Gradle Kotlin DSL (multi-module) |
| Persistence | Spring Data JPA + Flyway + PostgreSQL 16 |
| API docs | springdoc-openapi (Swagger UI) |
| Web UI | Thymeleaf |
| Security | Spring Security (permissive skeleton) |
| Observability | Actuator (`health`, `info`, `prometheus`) |
| CI | GitHub Actions + Dependabot |
| Packaging | Fat JAR + multi-stage Docker image |

## Module dependency graph

```text
┌─────────────────────────────────────────┐
│                   app                   │
│         (@SpringBootApplication)        │
└─────────────┬───────────┬───────────────┘
              │           │
      ┌───────▼───┐   ┌───▼────────────┐
      │    api    │   │      web       │
      │ REST+OpenAPI│ │ Thymeleaf MVC  │
      └───────┬───┘   └───┬────────────┘
              │           │
              └─────┬─────┘
                    │
          ┌─────────▼─────────┐
          │       domain      │
          │  models/services  │
          │  repository ports │
          └─────────▲─────────┘
                    │
          ┌─────────┴─────────┐
          │  infrastructure   │
          │ JPA, Flyway, Sec  │
          └───────────────────┘
```

`app` also depends on `infrastructure` so adapters and security are on the runtime classpath.

## Request flow (REST create)

```text
HTTP POST /api/v1/items
        │
        ▼
 ItemController (api)
        │ validate DTO
        ▼
 ItemService (domain)
        │
        ▼
 ItemRepository port (domain)
        │
        ▼
 ItemRepositoryAdapter (infrastructure)
        │
        ▼
 Spring Data JPA → PostgreSQL
        │
        ▼
 201 + ItemResponse + Location
```

Web form `POST /items` follows the same domain path via `ItemPageController`.

## Package layout

Base package: `com.boilerplate.springboot`

| Module | Packages |
|--------|----------|
| `app` | `com.boilerplate.springboot` |
| `api` | `...api.item`, `...api.config` |
| `web` | `...web`, `...web.item` |
| `domain` | `...domain.item` |
| `infrastructure` | `...infrastructure.config`, `...persistence`, `...web` |

## Configuration model

| Profile | Intent |
|---------|--------|
| *(default)* | Shared Actuator/Flyway/JPA defaults in `application.yml` |
| `local` | Local Postgres host + SQL logging |
| `test` | Quiet logging for automated tests |
| `prod` | Datasource from env vars; tighter logging/health details |

See [CONFIGURATION](../05-operations/CONFIGURATION.md).

## Security posture

`SecurityConfig` permits health, OpenAPI, `/api/v1/**`, and item pages without authentication. HTTP Basic remains available for other paths. Replace this before public production use.
