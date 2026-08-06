# Testing

## Pyramid in this repo

| Level | Where | Tooling |
|-------|-------|---------|
| Unit | `domain` | JUnit 5 + AssertJ, in-memory fakes |
| Integration | `app` | `@SpringBootTest`, MockMvc, **host Postgres** via `test` profile |

No Testcontainers / Docker engine is required.

## Run tests

```bash
make db-check   # Postgres must already exist
make test
```

Integration tests read `DB_HOST` / `DB_PORT` / `DB_NAME` / `DB_USER` / `DB_PASSWORD` (defaults match `application-local.yml`). If Postgres is unreachable they are **skipped** with a message pointing at `manage.sh db-init`.

CI uses a GitHub Actions **Postgres service** (not Docker Compose in the repo workflow).

## Writing a domain unit test

Mirror `ItemServiceTest`:

1. Fake `ItemRepository` in the test class
2. Construct `ItemService` with the fake
3. Assert behavior

No Spring context — keep these fast.

## Writing an integration test

Pattern from `ItemApiIntegrationTest`:

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WidgetApiIntegrationTest {

  @BeforeAll
  static void requirePostgres() { /* DriverManager ping or assumeTrue */ }

  @Autowired MockMvc mockMvc;
}
```

Use `@WithMockUser` when Security would otherwise challenge a path.

## Formatting gate

```bash
make check            # CI
make format           # fix locally
```

## Manual smoke

After changing the stack, run [FIRST_RUN_CHECKLIST](../01-getting-started/FIRST_RUN_CHECKLIST.md).
