plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
    id("com.gradle.plugin-publish") version "1.2.1"
    /// id("org.sirekanyan.version-checker")
}

group = "org.sirekanyan"
val appVersionName: String by properties
version = appVersionName

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.ktor:ktor-client-cio:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.17.2")
    testImplementation("junit:junit:4.13.2")
}

/// versionCheckerOptions {
///     "org.gradle.kotlin.kotlin-dsl" atMost org.gradle.kotlin.dsl.support.expectedKotlinDslPluginsVersion
///     "org.jetbrains.kotlin.plugin.serialization" atMost embeddedKotlinVersion
///     "org.jetbrains.kotlin" atMost embeddedKotlinVersion
/// }

gradlePlugin {
    website.set("https://sirekanyan.org/projects/version-checker")
    vcsUrl.set("https://github.com/sirekanian/version-checker")
    plugins {
        create("versionCheckerPlugin") {
            id = "org.sirekanyan.version-checker"
            implementationClass = "org.sirekanyan.versionchecker.VersionCheckerPlugin"
            displayName = "Latest Version Checker"
            description = "A Gradle plugin for checking the latest versions of dependencies"
            @Suppress("UnstableApiUsage")
            tags.set(listOf("latest", "version", "check", "checker", "versions", "dependency", "dependencies"))
        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}

kotlin {
    jvmToolchain(17)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
            allWarningsAsErrors = true
        }
    }
}
