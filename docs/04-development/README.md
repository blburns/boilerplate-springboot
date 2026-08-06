# Development

Guides for engineers extending this boilerplate.

| Doc | Description |
|-----|-------------|
| [ADDING_A_FEATURE](ADDING_A_FEATURE.md) | Vertical-slice recipe across modules |
| [API_REFERENCE](API_REFERENCE.md) | Items REST contract |
| [TESTING](TESTING.md) | Unit + Testcontainers integration tests |
| [CODING_CONVENTIONS](CODING_CONVENTIONS.md) | Style, packages, security hooks |

## Local loop

```bash
docker compose up -d postgres
./gradlew :app:bootRun --args='--spring.profiles.active=local'
./gradlew spotlessApply test
```

## Before opening a PR

1. `./gradlew spotlessApply`
2. `./gradlew test` (Docker required for integration tests)
3. `./gradlew :app:bootJar`
4. Smoke [First-Run Checklist](../01-getting-started/FIRST_RUN_CHECKLIST.md)

CI runs Spotless, tests, and `bootJar` on every PR to `main`.
