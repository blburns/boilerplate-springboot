# Using the API

End-user oriented guide for calling the sample Items REST API. Developers needing the full contract should also read [API Reference](../04-development/API_REFERENCE.md).

## Interactive: Swagger UI

1. Open **http://localhost:8080/swagger-ui.html**
2. Expand the **Items** tag
3. Use **Try it out** on any operation
4. Responses show status code and JSON body

## Base URL

```text
http://localhost:8080/api/v1/items
```

Content-Type for write operations: `application/json`

## Common workflows

### List all items

```bash
curl -s http://localhost:8080/api/v1/items | jq
```

### Create an item

```bash
curl -s -X POST http://localhost:8080/api/v1/items \
  -H 'Content-Type: application/json' \
  -d '{"name":"Notebook","description":"Lined"}' | jq
```

Expect **201 Created** and a `Location` header like `/api/v1/items/1`.

### Get one item

```bash
curl -s http://localhost:8080/api/v1/items/1 | jq
```

### Update an item

```bash
curl -s -X PUT http://localhost:8080/api/v1/items/1 \
  -H 'Content-Type: application/json' \
  -d '{"name":"Notebook","description":"Grid"}' | jq
```

### Delete an item

```bash
curl -s -o /dev/null -w "%{http_code}\n" \
  -X DELETE http://localhost:8080/api/v1/items/1
```

Expect **204** (no body).

## Error responses users may see

| HTTP | Meaning | Typical cause |
|------|---------|---------------|
| 400 | Validation failed | Missing `name`, field too long |
| 404 | Item not found | Wrong or deleted id |
| 500 | Unexpected error | Server/database problem — contact an admin |

Error bodies use Problem Details (`application/problem+json` when supported), with a message and optional field map under `errors`.

## Security reminder

Default local policy allows unauthenticated access to `/api/v1/**`. Treat credentials and environment URLs carefully in shared or production setups.
