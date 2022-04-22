import info.solidsoft.gradle.pitest.PitestTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("info.solidsoft.pitest") version "1.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("org.owasp.dependencycheck") version "6.5.3"
    id("org.springframework.boot") version "2.6.3"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
}

group = "com.alesaudate"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2021.0.1"
extra["testcontainersVersion"] = "1.16.2"
extra["tomcat.version"] = "9.0.58" // CVE-2022-23181

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.6")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("org.springframework.boot:spring-boot-starter-web")

    runtimeOnly("mysql:mysql-connector-java")

    testImplementation("com.ninja-squad:springmockk:3.1.0")
    testImplementation("io.rest-assured:spring-mock-mvc:4.5.1")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.pitest:pitest-junit5-plugin:0.15")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<PitestTask> {
    testPlugin.set("junit5")
    excludedClasses.set(
        setOf(
            "com.alesaudate.transactionservice.interfaces.outcoming.db.Account",
            "com.alesaudate.transactionservice.interfaces.outcoming.db.Transaction",
            "com.alesaudate.transactionservice.interfaces.incoming.http.**",
            "**\$Companion"
        )
    )
    mutationThreshold.set(90)
    excludedTestClasses.set(setOf("*.integration.*"))
    threads.set(8)
    timestampedReports.set(false)
    outputFormats.set(setOf("HTML"))
    reportDir.set(file("$buildDir/reports/pitest"))
    mutators.set(setOf("STRONGER", "DEFAULTS"))
    avoidCallsTo.set(setOf("kotlin.jvm.internal", "kotlinx.coroutines", "org.slf4j"))
    historyInputLocation.set(file("$projectDir/pitest/pitest.history"))
    historyOutputLocation.set(file("$projectDir/pitest/pitest.history"))
}

tasks.build {
    dependsOn("ktlintFormat")
}
