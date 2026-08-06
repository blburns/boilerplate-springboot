# Gradle Tasks

Run from the repository root. Prefer the wrapper (`./gradlew`).

## Everyday

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
