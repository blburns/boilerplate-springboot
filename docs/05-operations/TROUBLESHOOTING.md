# Troubleshooting

## Gradle fails with only a version number (e.g. `What went wrong: 26`)

**Cause:** An older Gradle daemon (pre-9) was running on JDK **26**. Some Gradle versions fail settings evaluation with a cryptic message that is just the major version number. Your debug log showed `javaVersion=26`.

This repo pins the wrapper to **Gradle 9.6+**, which can start on newer JDKs, while the **Java 21 toolchain** still compiles and runs the app.

**Fix (recommended — use JDK 21 for the daemon too):**

```bash
# macOS — select JDK 21
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Stop any daemon still bound to JDK 26
./gradlew --stop

./scripts/run-local.sh
# or:
./gradlew :app:bootRun --args='--spring.profiles.active=local'
```

Confirm:

```bash
java -version                 # prefer 21.x
./gradlew --version           # Gradle 9.6.x
./gradlew -q javaToolchains   # should list a JDK 21 for the toolchain
```

Avoid `--debug` unless needed; it is noisy and can leak sensitive environment details.

## App will not start — database connection

**Symptoms:** `Connection refused`, HikariPool timeout, Flyway cannot connect.

**Checks:**

1. `docker compose ps` — is `postgres` healthy?
2. Host/port: local profile defaults to `localhost:5432`; Compose app service must use `DB_HOST=postgres`
3. Credentials match Compose (`boilerplate` / `boilerplate`)
4. Port 5432 not taken by another Postgres

```bash
docker compose logs postgres
docker compose exec postgres pg_isready -U boilerplate -d boilerplate
```

## Flyway migration failure

**Symptoms:** Boot fails citing checksum mismatch or SQL error.

**Checks:**

1. Do not edit applied migrations that already shipped to shared DBs — add `V{n+1}__…`
2. Local reset (destroys data): `docker compose down -v && docker compose up -d postgres`
3. Confirm `V1__init_items.sql` is on the classpath (`infrastructure` jar)

## 404 on `/items` or API

**Checks:**

1. App actually listening (`curl -I http://localhost:8080/actuator/health`)
2. Context path not customized (default `/`)
3. Wrong profile without datasource causing failed context (inspect logs)

## Tests skipped or failing

| Observation | Cause | Fix |
|-------------|-------|-----|
| Integration tests skipped | No Docker | Start Docker Desktop / daemon |
| ContainerFetchException | Docker API unreachable | Fix Docker; CI already has Docker |
| Spotless failure | Formatting drift | `./gradlew spotlessApply` |

## Validation errors on create

API **400** with `errors.name` → send non-blank `name`.  
UI redisplays form → fill required Name field.

## Actuator 401 / 404

Only selected actuator paths are `permitAll`. Other endpoints may require auth or be unexposed (`management.endpoints.web.exposure.include`).

## Swagger blank or 403

Confirm Security still permits `/swagger-ui/**` and `/v3/api-docs/**`. Hardened forks often lock these — enable temporarily for debugging.

## Image build fails in CI

Inspect `docker.yml` / Dockerfile stage:

- Wrapper present (`gradlew`)
- Network for dependency download
- GHCR permissions (`packages: write`) for push

## Still stuck

1. Collect `./gradlew :app:bootRun --args='--spring.profiles.active=local'` startup log
2. `docker compose logs app postgres`
3. Confirm Java 21: `java -version`
