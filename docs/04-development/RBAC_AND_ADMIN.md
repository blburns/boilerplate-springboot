# Admin Shell & RBAC

Thymeleaf admin UI with form login and permission-based access control.

## Default credentials (local seed)

| Email | Password | Role |
|-------|----------|------|
| `admin@example.com` | `admin123` | `ADMIN` |

Seeded on first boot when the `users` table is empty (`AdminUserSeeder`). **Change before production.**

## Permissions

| Code | Meaning |
|------|---------|
| `admin:access` | Enter `/admin` shell |
| `items:read` / `items:write` | Items UI + API |
| `users:read` / `users:write` | User admin |
| `roles:read` / `roles:write` | Roles view (write reserved) |

Roles `ADMIN` (all permissions) and `USER` (`admin:access` + items) are seeded by Flyway `V3__rbac_seed.sql`.

## URLs

| Path | Notes |
|------|-------|
| `/login` | Form login |
| `/admin` | Dashboard |
| `/items` | Items CRUD in shell |
| `/admin/users` | User management |
| `/admin/roles` | Role / permission browser |

## Security model

- Session cookies + CSRF for Thymeleaf forms
- CSRF ignored for `/api/**` (API uses session or HTTP Basic with authorities)
- Authorities are permission codes plus `ROLE_{name}`

## Extending

1. Insert permission rows (Flyway)
2. Attach to roles via `role_permissions`
3. Protect routes in `SecurityConfig` with `hasAuthority("…")`
4. Gate nav/actions with `sec:authorize="hasAuthority('…')"`
