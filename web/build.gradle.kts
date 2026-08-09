plugins {
    `java-library`
}

dependencies {
    api(project(":domain"))

    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.security)
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

    testImplementation(libs.spring.boot.starter.test)
}
