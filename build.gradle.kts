plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version embeddedKotlinVersion
    id("com.gradle.plugin-publish") version "1.2.0"
    id("org.sirekanyan.version-checker")
}

group = "org.sirekanyan"
val appVersionName: String by properties
version = appVersionName

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
}

gradlePlugin {
    @Suppress("UnstableApiUsage")
    website.set("https://sirekanyan.org/projects/version-checker")
    @Suppress("UnstableApiUsage")
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
    jvmToolchain(11)
}

tasks {
    compileKotlin {
        kotlinOptions {
            allWarningsAsErrors = true
        }
    }
}
