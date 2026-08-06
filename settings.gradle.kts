rootProject.name = "boilerplate-springboot"

// App/toolchain target is Java 21. The Gradle daemon may use a newer JDK; prefer 21 locally.
val daemonJavaMajor = Runtime.version().feature()
if (daemonJavaMajor < 17) {
    throw GradleException(
        """
        Unsupported JDK $daemonJavaMajor for the Gradle daemon (need 17+; this project targets 21).

        Fix (macOS example):
          export JAVA_HOME=${'$'}(/usr/libexec/java_home -v 21)
          ./gradlew --stop

        See docs/05-operations/TROUBLESHOOTING.md.
        """.trimIndent(),
    )
}
if (daemonJavaMajor !in 17..24) {
    logger.warn(
        "Gradle daemon is JDK $daemonJavaMajor; this project compiles/runs with Java 21 toolchain. " +
            "Prefer JAVA_HOME pointing at JDK 21 for fewer surprises.",
    )
}

include(
    "app",
    "api",
    "web",
    "domain",
    "infrastructure",
)
