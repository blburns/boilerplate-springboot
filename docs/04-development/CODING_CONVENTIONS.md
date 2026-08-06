# Coding Conventions

## Language and style

- Java 21 language level (toolchain enforced in Gradle)
- Prefer constructor injection; avoid field `@Autowired`
- Records for API DTOs when immutability fits
- Domain exceptions for expected business failures
- Spotless: Google Java Format — run `spotlessApply` before commit

## Packages

Stay under `com.boilerplate.springboot.<module>...`. Do not create a second top-level root without updating `scanBasePackages` on `Application`.

## Persistence

- Schema changes go through **Flyway only** (`ddl-auto: validate`)
- Never put `@Entity` on domain models
- Name migrations `V{n}__short_description.sql` with monotonic `n`

## API

- Prefix public HTTP APIs with `/api/v1`
- Return Problem Details for errors (extend `ApiExceptionHandler`)
- Annotate controllers for springdoc (`@Tag`, `@Operation`)

## Web

- Keep templates under `web/src/main/resources/templates`
- Prefer progressive enhancement over JS frameworks in this base
- Reuse existing CSS variables in `static/css/app.css` when possible

## Security

- Default filter chain is **demo-open** — treat every new path as closed unless intentionally listed in `SecurityConfig`
- Disable CSRF carefully; if you enable cookie sessions for forms, re-enable CSRF tokens
- Production: introduce OAuth2 resource server or form login; externalize secrets via env

## Git

Prefer small commits that match the feature recipe steps (domain → infrastructure → api/web → tests → docs).

## Do not commit

- `.tools/` (local JDKs)
- `.env` with secrets
- `**/build/`
- IDE metadata already covered by `.gitignore`
