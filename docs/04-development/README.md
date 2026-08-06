# Development

Guides for engineers extending this boilerplate.

| Doc | Description |
|-----|-------------|
| [ADDING_A_FEATURE](ADDING_A_FEATURE.md) | Vertical-slice recipe across modules |
| [API_REFERENCE](API_REFERENCE.md) | Items REST contract |
| [TESTING](TESTING.md) | Unit + Postgres-backed integration tests |
| [CODING_CONVENTIONS](CODING_CONVENTIONS.md) | Style, packages, security hooks |

## Local loop

```bash
make db-check
make run
make format test
```

## Before opening a PR

1. `make format`
2. `make test` (Postgres must be reachable)
3. `make jar`
4. Smoke [First-Run Checklist](../01-getting-started/FIRST_RUN_CHECKLIST.md)

CI runs Spotless, tests (GitHub Actions Postgres service), and `bootJar` on every PR to `main`.
