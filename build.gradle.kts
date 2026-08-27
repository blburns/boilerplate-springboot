plugins {
    java
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spotless)
}

allprojects {
    group = "com.boilerplate.springboot"
    version = "0.1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.diffplug.spotless")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    if (name != "domain") {
        dependencies {
            "implementation"(platform("org.springframework.boot:spring-boot-dependencies:4.1.1"))
            "testImplementation"(platform("org.springframework.boot:spring-boot-dependencies:4.1.1"))
        }
    }

    spotless {
        java {
            target("src/**/*.java")
            googleJavaFormat("1.28.0")
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }
}
