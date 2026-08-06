# Overview

Boilerplate Spring Boot is a **cloneable application base** for building new services on:

- Java **21**
- Spring Boot **4.1**
- Gradle (Kotlin DSL), multi-module
- PostgreSQL + Flyway
- REST (OpenAPI) + optional Thymeleaf UI
- Actuator health/metrics
- GitHub Actions CI and GHCR Docker publish

It ships a sample **Item** resource end-to-end so you can see how domain ports, JPA adapters, REST, and SSR web pages fit together—then replace or extend that sample for your product.

## Who this is for

| Role | Goal |
|------|------|
| **End user** | Try the sample Items UI / API to understand what the base app can do today |
| **Developer** | Clone, add features in the correct module, keep tests green |
| **Administrator** | Deploy, configure profiles/secrets, monitor health, diagnose failures |

## Design principles

1. **Clear module boundaries** — presentation (`api` / `web`) depends on `domain`; persistence and security live in `infrastructure`
2. **Domain without Spring** — business logic stays unit-testable without a container
3. **Profile-driven config** — `local` / `test` / `prod` YAMLs plus env vars
4. **Runnable day one** — host Postgres + `make run`, Swagger, health endpoint
5. **CI by default** — format check, tests against a CI Postgres service, and fat JAR on every PR

## What ships (sample)

| Surface | Path | Purpose |
|---------|------|---------|
| Web UI | `/items` | List, create, delete items |
| REST API | `/api/v1/items` | Full CRUD JSON API |
| OpenAPI | `/swagger-ui.html` | Interactive API docs |
| Health | `/actuator/health` | Liveness/readiness style check |

## Explicit non-goals (v1)

- Production-ready authentication (OAuth2 / JWT UI) — **skeleton only**
- Kubernetes / Helm charts
- Messaging (Kafka/Rabbit), Redis cache
- SPA frontend (React/Vue) — Thymeleaf covers SSR; add a client later if needed

## Next steps

- Install: [Getting Started](01-getting-started/GETTING_STARTED.md)
- Use the sample: [User Guide](02-user-guide/README.md)
- Extend it: [Adding a Feature](04-development/ADDING_A_FEATURE.md)
- Operate it: [Admin Guide](05-operations/ADMIN_GUIDE.md)
