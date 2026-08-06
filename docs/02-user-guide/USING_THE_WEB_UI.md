# Using the Web UI

## Open the Items page

1. Start the app ([Getting Started](../01-getting-started/GETTING_STARTED.md)).
2. Visit **http://localhost:8080/items** (or `/`, which redirects there).

You should see:

- Brand header (**Boilerplate**)
- **Create item** form
- **All items** table (empty on first run)
- Footer links to API docs and health

## Create an item

1. Enter a **Name** (required).
2. Optionally enter a **Description**.
3. Click **Add item**.

On success you return to the list with a flash message and the new row visible.

### Validation

| Condition | Result |
|-----------|--------|
| Empty name | Form redisplays with a field error; nothing is saved |
| Name longer than 200 characters | Rejected by validation |
| Description longer than 2000 characters | Rejected by validation |

## List items

The table shows ID, name, description, and a **Delete** action per row.

There is no paging in the sample UI: all items are listed.

## Delete an item

1. Click **Delete** on the row.
2. The page refreshes; a flash message confirms removal.

If the item was already deleted (stale page), you may see an error flash instead.

## Tips

- Prefer Chrome/Firefox/Safari recent versions.
- For bulk or scripted work, use the [API](USING_THE_API.md) instead of the form.
- Footer **API docs** opens Swagger; **Health** opens Actuator JSON.
