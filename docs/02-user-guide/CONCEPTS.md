# Concepts

## What is this application?

For end users, this deployment is a small **Items catalog**: you can create named records with an optional description, list them, and delete them.

Under the hood it is a multi-module Spring Boot base app; as a user you only need the browser UI or the HTTP API.

## Item

An **Item** has:

| Field | Required | Limits | Meaning |
|-------|----------|--------|---------|
| `id` | (assigned) | Positive long | Stable identifier |
| `name` | Yes | Max 200 characters | Short label |
| `description` | No | Max 2000 characters | Free-text notes |
| `createdAt` / `updatedAt` | (system) | Instant (UTC) | Audit timestamps |

## Surfaces

```text
Browser ──► /items          (Thymeleaf forms)
HTTP client ──► /api/v1/items  (JSON REST)
Swagger UI ──► /swagger-ui.html
```

Both UI and API read/write the same Postgres `items` table. Changes in one surface appear in the other after refresh.

## Security note for users

You must **sign in** at `/login` to use the admin shell and Items UI. Access to Users/Roles depends on your assigned permissions. Ask an administrator if you cannot open a page.

## Typical use cases

1. **Smoke-test a new environment** — create and delete an item after deploy
2. **API client prototyping** — use Swagger or curl against `/api/v1/items`
3. **Training / onboarding** — walk new developers through a complete vertical slice without domain complexity
