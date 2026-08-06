# Monitoring

## Health

```bash
curl -s http://localhost:8080/actuator/health | jq
```

Healthy example:

```json
{
  "status": "UP"
}
```

With details enabled (non-prod when authorized): components such as `db` / `diskSpace` may appear.

Use this URL for:

- Load balancer health checks
- Compose / Kubernetes probes (`/actuator/health`)

## Info

```bash
curl -s http://localhost:8080/actuator/info | jq
```

Includes application name/description/version from `application.yml` `info.app.*`.

## Prometheus metrics

Endpoint: `/actuator/prometheus`

Expose when `micrometer-registry-prometheus` is on the classpath (shipped in `app`).

Scraping example (Prometheus config sketch):

```yaml
scrape_configs:
  - job_name: boilerplate-springboot
    metrics_path: /actuator/prometheus
    static_configs:
      - targets: ["app:8080"]
```

Protect this endpoint in production (network policy or auth).

## Logs

Default Logback console logging via Spring Boot.

| Profile | Expect |
|---------|--------|
| `local` | Hibernate SQL + DEBUG app logs |
| `prod` | WARN/INFO — ship stdout to your log aggregator |

Look for Flyway lines on startup (`Successfully applied … migration`).

## Alerting suggestions

| Signal | Severity |
|--------|----------|
| Health not UP | Page / ticket |
| Elevated 5xx rate | Investigate app + DB |
| Flyway failed at boot | Block rollouts |
| Disk / connection pool exhaustion | Capacity |

(Wire alerting in your org’s stack; this boilerplate does not ship alert rules.)
