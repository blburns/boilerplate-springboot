# Gradle Tasks

Prefer the project control plane first:

```bash
make help
./scripts/manage.sh help
```

See [scripts/README.md](../../scripts/README.md). Direct Gradle remains available via the wrapper (`./gradlew`).

## Everyday (via Make / manage.sh)

| Make | manage.sh | Purpose |
|------|-----------|---------|
| `make run` / `run-db` | `run` / `run --with-db` | bootRun (+ optional Postgres) |
| `make test` | `test` | All module tests |
| `make format` | `format` | Spotless apply |
| `make check` | `check` | Spotless check |
| `make jar` | `jar` | Fat JAR |
| `make verify` | `verify` | CI-like: check + test + jar |
| `make clean` | `clean` | Delete build outputs |

## Everyday (raw Gradle)

| Task | Purpose |
|------|---------|
| `:app:bootRun` | Run the app (pass `--args='--spring.profiles.active=local'`) |
| `test` | All module tests |
| `spotlessApply` | Format Java + Gradle Kotlin DSL |
| `spotlessCheck` | Verify formatting (CI) |
| `:app:bootJar` | Build executable fat JAR |
| `clean` | Delete build outputs |

## Examples

```bash
./gradlew :app:bootRun --args='--spring.profiles.active=local'
./gradlew spotlessApply test
./gradlew :app:bootJar -x test
./gradlew :domain:test
./gradlew :app:dependencies --configuration runtimeClasspath
```

## Multi-module tips

- Qualify tasks with project path: `:infrastructure:compileJava`
- Only `app` applies the Spring Boot plugin for packaging
- Library modules produce plain JARs consumed by `app`

## Toolchain / wrapper

- Wrapper: **Gradle 9.6.x** (`./gradlew --version`)
- Compile/run target: Java **21** via Gradle toolchain (ensure a JDK 21 is installed or Foojay can download one)
- Prefer `JAVA_HOME` → JDK 21 even if other JDKs are installed
