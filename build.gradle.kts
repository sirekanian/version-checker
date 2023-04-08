plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.sirekanyan"
version = "0.0.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.ktor:ktor-client-cio:2.1.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.4")
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
