# Modules

## Responsibility matrix

| Module | Boot plugin? | Depends on | Owns |
|--------|--------------|------------|------|
| `app` | Yes (fat JAR) | `api`, `web`, `infrastructure`, `domain` | Entry class, YAML profiles, integration tests |
| `api` | No | `domain` | REST controllers, request/response DTOs, OpenAPI bean |
| `web` | No | `domain` | Thymeleaf controllers, templates, static CSS |
| `domain` | No | *(none)* | Models, exceptions, ports, services |
| `infrastructure` | No | `domain` | JPA entities/repos/adapters, Flyway SQL, Security, API `@RestControllerAdvice` |

## Dependency rules

**Allowed**

- `api` → `domain`
- `web` → `domain`
- `infrastructure` → `domain`
- `app` → everything (composition root)

**Forbidden**

- `domain` → Spring, JPA, or web frameworks
- `domain` → `api` / `web` / `infrastructure`
- `api` → `infrastructure` or `web` (talk through domain ports/services)
- `web` → `api` or `infrastructure`

Wire Spring beans for domain services in `infrastructure` `DomainConfig` (or a dedicated config next to adapters).

## Sample feature map (Item)

| Concern | Location |
|---------|----------|
| Model + port + service | `domain/.../item/` |
| Unit tests | `domain/src/test/.../ItemServiceTest.java` |
| JPA entity + adapter | `infrastructure/.../persistence/` |
| Flyway | `infrastructure/src/main/resources/db/migration/V1__init_items.sql` |
| REST | `api/.../item/ItemController.java` |
| Thymeleaf | `web/.../item/ItemPageController.java` + `templates/items/list.html` |
| Integration tests | `app/src/test/.../ItemApiIntegrationTest.java` |

## Adding another module later

Examples: `messaging`, `batch`, `client-api`. Prefer:

1. Create Gradle subproject
2. `include(...)` in `settings.gradle.kts`
3. Depend only downward (toward `domain`) or laterally via shared contracts
4. Have `app` pull the module onto the runtime classpath
