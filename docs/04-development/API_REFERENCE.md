# API Reference

Base path: `/api/v1/items`  
OpenAPI UI: `/swagger-ui.html`  
OpenAPI JSON: `/v3/api-docs`

## Resource: Item

### Item JSON

```json
{
  "id": 1,
  "name": "Notebook",
  "description": "Lined",
  "createdAt": "2026-08-05T12:00:00Z",
  "updatedAt": "2026-08-05T12:00:00Z"
}
```

### Write body (`ItemRequest`)

```json
{
  "name": "Notebook",
  "description": "Lined"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `name` | string | yes | not blank, ≤ 200 |
| `description` | string | no | ≤ 2000 |

---

## Endpoints

### `GET /api/v1/items`

List all items.

**200** — JSON array of Item

### `GET /api/v1/items/{id}`

Fetch one item.

**200** — Item  
**404** — Problem Detail (`Item not found: {id}`)

### `POST /api/v1/items`

Create an item.

**201** — Item; `Location: /api/v1/items/{id}`  
**400** — Validation Problem Detail with `errors` map

### `PUT /api/v1/items/{id}`

Replace name/description.

**200** — Item  
**400** — Validation failure  
**404** — Missing id

### `DELETE /api/v1/items/{id}`

**204** — Empty body  
**404** — Missing id

---

## Error format

Unhandled and domain errors for API packages are rendered as Spring **Problem Details**:

```json
{
  "type": "https://boilerplate.local/problems/item-not-found",
  "title": "Item Not Found",
  "status": 404,
  "detail": "Item not found: 99",
  "itemId": 99
}
```

Validation example property:

```json
"errors": {
  "name": "name is required"
}
```

---

## Versioning

Paths are prefixed with `/api/v1`. Introduce `/api/v2` for breaking changes rather than mutating v1 clients in place.
