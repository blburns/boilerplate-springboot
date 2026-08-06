# Project scripts

| Script / entry | Purpose |
|----------------|---------|
| [`manage.sh`](manage.sh) | Primary control plane (run, test, build, db-check, …) |
| [`run-local.sh`](run-local.sh) | Alias for `manage.sh run` |
| [`sql/init_local_db.sql`](sql/init_local_db.sql) | Create local role/database |
| Root [`Makefile`](../Makefile) | `make <target>` delegates to `manage.sh` |

Postgres is expected to be **already installed** on the host (or otherwise reachable). Docker Compose is optional only.

## Quick start

```bash
make db-init         # once: create role + database (needs psql superuser)
make db-check        # verify connection
make run             # bootRun against local Postgres

make help
./scripts/manage.sh help
```

## Common targets

| Make | Purpose |
|------|---------|
| `make run` | bootRun (`local` profile) |
| `make db-init` / `db-check` | Provision / verify Postgres |
| `make test` | Unit + integration tests |
| `make verify` | Spotless + test + bootJar |
| `make format` | Spotless apply |
| `make jar` | Fat JAR |
| `make status` | JDK / Postgres / health |

Optional Compose (appendix only): `make compose-up` / `compose-down`.
