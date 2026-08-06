# Deployment

## Artifact

```bash
./gradlew :app:bootJar
```

Output: `app/build/libs/boilerplate-springboot-0.1.0-SNAPSHOT.jar`

Run:

```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:postgresql://… 
export SPRING_DATASOURCE_USERNAME=…
export SPRING_DATASOURCE_PASSWORD=…
java -jar app/build/libs/boilerplate-springboot-0.1.0-SNAPSHOT.jar
```

Ensure Postgres is reachable and empty/migrations can apply **before** traffic is routed.

## Optional: Docker Compose

Not required. Prefer a host or managed Postgres + fat JAR. If you want containers:

```bash
make compose-up
```

## Optional: Container image (GHCR)

Workflow [`.github/workflows/docker.yml`](../../.github/workflows/docker.yml):

- Triggers: push to `main`, tags `v*`
- Image: `ghcr.io/<owner>/<repo>`
- Tags: `latest` (default branch), git SHA, version tag

Pull/run example:

```bash
docker pull ghcr.io/<owner>/<repo>:latest
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/boilerplate \
  -e SPRING_DATASOURCE_USERNAME=boilerplate \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  ghcr.io/<owner>/<repo>:latest
```

## CI gate before promote

[`.github/workflows/ci.yml`](../../.github/workflows/ci.yml) on `main` / PRs:

1. Spotless
2. Tests (CI Postgres service)
3. `bootJar`

Do not promote a failed CI build.

## Reverse proxy tips

- Forward `X-Forwarded-*` if TLS terminates upstream
- Restrict `/actuator/**` at the proxy if not required publicly
- Consider blocking `/swagger-ui.html` and `/v3/api-docs` in production

## Rollback

1. Redeploy previous JAR/image tag
2. Flyway migrations are forward-only — prepare reverse SQL carefully if you must undo a schema change (prefer additive migrations)
