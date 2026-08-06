# First-Run Checklist

Use this after `make db-init` and `make run`.

## Postgres

- [ ] `make db-check` succeeds
- [ ] `psql -h localhost -U boilerplate -d boilerplate -c '\dt'` works after first app boot (Flyway creates `items`)

## Health

- [ ] `GET http://localhost:8080/actuator/health` returns `"status":"UP"`

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

## Database (after Flyway)

```bash
psql -h localhost -U boilerplate -d boilerplate -c '\dt'
```

You should see `items` and `flyway_schema_history`.

## Tests

- [ ] `make test` is green against the same local Postgres

## If something fails

See [TROUBLESHOOTING](../05-operations/TROUBLESHOOTING.md).
