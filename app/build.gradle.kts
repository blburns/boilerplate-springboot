plugins {
    alias(libs.plugins.spring.boot)
    `java-library`
}

dependencies {
    implementation(project(":api"))
    implementation(project(":web"))
    implementation(project(":infrastructure"))
    implementation(project(":domain"))

    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.micrometer.registry.prometheus)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.spring.boot.starter.security.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveBaseName.set("boilerplate-springboot")
}

tasks.named<Jar>("jar") {
    enabled = false
}
