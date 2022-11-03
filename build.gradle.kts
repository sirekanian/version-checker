plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "com.sirekanyan"
version = "0.0.4"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:4.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.1")
    implementation("io.ktor:ktor-client-cio:1.4.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.11.3")
}

gradlePlugin {
    plugins {
        create("Check dependency versions") {
            id = "bump-plugin"
            implementationClass = "com.sirekanyan.bump.BumpPlugin"
        }
    }
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

publishing {
    repositories {
        mavenLocal()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
