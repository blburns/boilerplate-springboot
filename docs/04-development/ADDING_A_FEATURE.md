# Adding a Feature

Follow this recipe to add a new business capability the same way **Item** is structured.

Example below assumes a new resource named **Widget**.

## 1. Domain

Create under `domain/src/main/java/com/boilerplate/springboot/domain/widget/`:

1. `Widget` — immutable or carefully mutated model
2. `WidgetRepository` — port interface (`save`, `findById`, `findAll`, …)
3. `WidgetNotFoundException` — domain exception
4. `WidgetService` — use cases (plain Java constructor injection)

Add unit tests under `domain/src/test/...` with an in-memory fake repository (see `ItemServiceTest`).

## 2. Infrastructure

1. **Flyway** — `infrastructure/src/main/resources/db/migration/V2__init_widgets.sql`
2. **JPA entity** — `WidgetEntity` + `SpringDataWidgetRepository`
3. **Adapter** — `WidgetRepositoryAdapter implements WidgetRepository`
4. **Bean wiring** — register `WidgetService` in `DomainConfig` (or a new `@Configuration`)

```java
@Bean
WidgetService widgetService(WidgetRepository widgetRepository) {
  return new WidgetService(widgetRepository);
}
```

Keep JPA annotations out of `domain`.

## 3. API (optional)

Under `api/.../widget/`:

- `WidgetRequest` / `WidgetResponse` (Jakarta Validation)
- `WidgetController` at `/api/v1/widgets`
- OpenAPI `@Tag` / `@Operation`

Problem Details for not-found/validation already come from `ApiExceptionHandler` if exceptions are domain types it knows—or extend the advice.

## 4. Web (optional)

Under `web/.../widget/`:

- MVC `@Controller` + form object
- Thymeleaf template under `web/src/main/resources/templates/`
- CSS only if needed (`static/css/`)

## 5. Security

Update `SecurityConfig` if the new paths should be public or protected. Do not leave new admin endpoints open by accident.

## 6. Tests in `app`

Add `@SpringBootTest` + Testcontainers coverage for the happy path (create/get/delete) next to `ItemApiIntegrationTest`.

## 7. Docs

Update:

- [API_REFERENCE](API_REFERENCE.md) (if REST)
- [DATABASE_SCHEMA](../06-reference/DATABASE_SCHEMA.md)
- User guide if end users gain a new page

## Checklist

- [ ] Domain compiles with no Spring imports
- [ ] Flyway migration is additive and indexed as needed
- [ ] Spotless clean
- [ ] Unit + integration tests
- [ ] Swagger shows the new operations
- [ ] `local` profile still boots against Compose Postgres
