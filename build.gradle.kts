import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
}

group = "com.codefriendz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    google()
}
ktlint {
    disabledRules.set(setOf("no-wildcard-imports"))
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation("io.arrow-kt:arrow-core:1.1.5")

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // postgres
    implementation("org.postgresql:postgresql")
    implementation("org.postgresql:r2dbc-postgresql")

    // flyway
    implementation("org.flywaydb:flyway-core:9.12.0")

    // Swaggerx`
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.12")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.12")

    // Validators
    implementation("commons-validator:commons-validator:1.7")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.5")

    // Testing
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging.showStandardStreams = true
}
