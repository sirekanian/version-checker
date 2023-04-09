pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        val appVersionName: String by settings
        id("bump-plugin") version appVersionName
    }
}

rootProject.name = "bump"
