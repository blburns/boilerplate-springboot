# First-Run Checklist

Use this after your first `bootRun` or `docker compose up`.

## Health

- [ ] `GET http://localhost:8080/actuator/health` returns `"status":"UP"`
- [ ] Postgres container is healthy: `docker compose ps`

## Web UI

- [ ] Open http://localhost:8080/items
- [ ] Create an item with a name and optional description
- [ ] Confirm it appears in the table
- [ ] Delete the item and confirm it is removed

## API / OpenAPI

- [ ] Open http://localhost:8080/swagger-ui.html
- [ ] Expand **Items** → **POST /api/v1/items** → Try it out
- [ ] Create `{ "name": "Checklist item", "description": "ok" }` → **201**
- [ ] **GET /api/v1/items** lists the new record

## Database

- [ ] Flyway applied `V1__init_items.sql` (no migration errors in logs)
- [ ] Optional: `\dt` in `psql` shows `items` and `flyway_schema_history`

```bash
docker compose exec postgres \
  psql -U boilerplate -d boilerplate -c '\dt'
```

## Tests (developer machines with Docker)

- [ ] `./gradlew test` is green (integration tests need Docker)

## If something fails

See [TROUBLESHOOTING](../05-operations/TROUBLESHOOTING.md).
