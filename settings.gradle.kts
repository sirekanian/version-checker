pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        val appVersionName: String by settings
        id("org.sirekanyan.version-checker") version appVersionName
    }
}

rootProject.name = "version-checker"
