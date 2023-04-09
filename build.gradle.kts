plugins {
    `kotlin-dsl`
    `maven-publish`
    id("bump-plugin")
}

group = "com.sirekanyan"
val appVersionName: String by properties
version = appVersionName

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
}

gradlePlugin {
    plugins {
        create("Check dependency versions") {
            id = "bump-plugin"
            implementationClass = "com.sirekanyan.bump.BumpPlugin"
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
