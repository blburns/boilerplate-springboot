# Database Schema

## Engine

PostgreSQL **14+** (16 recommended), expected as a host service.

Migrations: Flyway under `infrastructure/src/main/resources/db/migration/`.

Bootstrap role/database: [`scripts/sql/init_local_db.sql`](../../scripts/sql/init_local_db.sql) via `make db-init`.

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
psql -h localhost -U boilerplate -d boilerplate -c '\d+ items'
```

## Adding tables

1. New file `V2__….sql` (never rewrite `V1` after shared use)
2. JPA entity + adapter in `infrastructure`
3. Domain port/service
4. Document here

See [ADDING_A_FEATURE](../04-development/ADDING_A_FEATURE.md).

## Backup / restore (admin)

```bash
pg_dump -h localhost -U boilerplate boilerplate > backup.sql
psql -h localhost -U boilerplate boilerplate < backup.sql
```
