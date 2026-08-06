# Database Schema

## Engine

PostgreSQL **16** (Compose image `postgres:16-alpine`).

Migrations: Flyway under `infrastructure/src/main/resources/db/migration/`.

## Applied migrations

### `V1__init_items.sql`

Creates the sample Items table.

| Column | Type | Null | Notes |
|--------|------|------|-------|
| `id` | `BIGSERIAL` | no | Primary key |
| `name` | `VARCHAR(200)` | no | Indexed |
| `description` | `VARCHAR(2000)` | yes | |
| `created_at` | `TIMESTAMPTZ` | no | Default `NOW()` |
| `updated_at` | `TIMESTAMPTZ` | no | Default `NOW()`; also maintained by JPA `@PreUpdate` |

Index: `idx_items_name` on (`name`).

Flyway also maintains `flyway_schema_history`.

## Inspect locally

```bash
docker compose exec postgres \
  psql -U boilerplate -d boilerplate -c '\d+ items'
```

## Adding tables

1. New file `V2__….sql` (never rewrite `V1` after shared use)
2. JPA entity + adapter in `infrastructure`
3. Domain port/service
4. Document here

See [ADDING_A_FEATURE](../04-development/ADDING_A_FEATURE.md).

## Backup / restore (admin)

```bash
# backup
docker compose exec -T postgres \
  pg_dump -U boilerplate boilerplate > backup.sql

# restore (destructive overview — use carefully)
docker compose exec -T postgres \
  psql -U boilerplate boilerplate < backup.sql
```
