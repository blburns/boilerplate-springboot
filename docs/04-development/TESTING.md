# Testing

## Pyramid in this repo

| Level | Where | Tooling |
|-------|-------|---------|
| Unit | `domain` | JUnit 5 + AssertJ, in-memory fakes |
| Integration | `app` | `@SpringBootTest`, MockMvc, Testcontainers Postgres |

## Run tests

```bash
./gradlew test
```

Integration tests require a working Docker daemon. Without Docker they are **skipped** (`@Testcontainers(disabledWithoutDocker = true)` + assumptions) so local machines without Docker can still run domain unit tests.

CI (`ubuntu-latest`) has Docker, so integration tests run on every PR.

## Writing a domain unit test

Mirror `ItemServiceTest`:

1. Fake `ItemRepository` implementation in the test class
2. Construct `ItemService` with the fake
3. Assert behavior (create/update/delete/not-found)

No Spring context — keep these fast.

## Writing an integration test

Pattern from `ItemApiIntegrationTest`:

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class WidgetApiIntegrationTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16-alpine");

  @Autowired MockMvc mockMvc;
  // exercise HTTP against real Flyway schema
}
```

`@ServiceConnection` wires datasource properties automatically.

Use `@WithMockUser` if Spring Security filters would otherwise challenge a path.

## Formatting gate

```bash
./gradlew spotlessCheck   # CI
./gradlew spotlessApply   # fix locally
```

Google Java Format + ktlint for Gradle Kotlin DSL.

## Manual smoke

After changing the stack, run [FIRST_RUN_CHECKLIST](../01-getting-started/FIRST_RUN_CHECKLIST.md).
