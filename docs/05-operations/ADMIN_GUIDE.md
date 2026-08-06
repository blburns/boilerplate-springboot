# Admin Guide

Day-2 operations for administrators running this boilerplate (or products derived from it).

## Responsibilities

| Area | Doc |
|------|-----|
| Profiles, env vars, secrets | [CONFIGURATION](CONFIGURATION.md) |
| Deploy JAR / optional image | [DEPLOYMENT](DEPLOYMENT.md) |
| Health and metrics | [MONITORING](MONITORING.md) |
| Incidents and common errors | [TROUBLESHOOTING](TROUBLESHOOTING.md) |

## Default topology

```text
Client ──► App (:8080) ──► PostgreSQL (host or managed)
              │
              └── Actuator (/actuator/*)
```

Default local DB: `boilerplate` @ `localhost:5432` (see `make db-init`).

## Security baseline (must harden for production)

Out of the box:

- CSRF disabled
- `/api/v1/**`, `/items/**`, Swagger, and selected Actuator routes are **permitAll**
- HTTP Basic can challenge other routes

Before public exposure:

1. Restrict `authorizeHttpRequests` to authenticated users where needed
2. Turn off or protect Swagger in `prod`
3. Limit Actuator exposure (`management.endpoints.web.exposure.include`)
4. Supply strong DB passwords via secrets manager / env
5. Set `SPRING_PROFILES_ACTIVE=prod` and required datasource env vars

## Data ownership

| Concern | Owner |
|---------|-------|
| Schema | Flyway migrations (app boot) |
| Backups | Admin — `pg_dump` / managed DB snapshots |
| App config | Env vars + `application-*.yml` |

## Routine checks

Daily / after deploy:

```bash
curl -sf http://localhost:8080/actuator/health
make db-check
```

Confirm `"status":"UP"`. Investigate DB connectivity if status is `DOWN`.

## User administration

The sample Items app has **no user accounts**. Authentication/RBAC must be added as a product feature; until then, treat network access control (VPN, reverse proxy auth, private subnet) as your perimeter.
