# Project scripts

| Script / entry | Purpose |
|----------------|---------|
| [`manage.sh`](manage.sh) | Primary control plane (run, test, build, db, compose, …) |
| [`run-local.sh`](run-local.sh) | Alias for `manage.sh run` |
| Root [`Makefile`](../Makefile) | `make <target>` delegates to `manage.sh` |

## Quick start

```bash
make db-up          # Postgres
make run            # Spring Boot local profile
# or in one step:
make run-db

make help           # full command list
./scripts/manage.sh help
```

## Common targets

| Make | Equivalent |
|------|------------|
| `make run` | `./scripts/manage.sh run` |
| `make run-db` | `./scripts/manage.sh run --with-db` |
| `make test` | `./scripts/manage.sh test` |
| `make verify` | spotlessCheck + test + bootJar |
| `make format` | Spotless apply |
| `make jar` | Fat JAR |
| `make up` / `make down` | Full Compose stack |
| `make status` | JDK / Docker / health |
| `make db-reset` | Wipe Postgres volume and recreate |

Pass extra args to `run` via `make run ARGS='--with-db'` when needed.
