package org.sirekanyan.versionchecker

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.SelfResolvingDependency
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.create
import org.sirekanyan.versionchecker.composechecker.ComposeVersionChecker
import org.sirekanyan.versionchecker.extensions.callMemberProperty
import org.sirekanyan.versionchecker.extensions.getSettingsRepositories
import org.sirekanyan.versionchecker.gradlechecker.GradleVersionChecker
import org.sirekanyan.versionchecker.model.Version
import org.sirekanyan.versionchecker.model.toVersion

class VersionCheckerPlugin : Plugin<Project> {

    private lateinit var extension: VersionCheckerExtension

    override fun apply(project: Project) {
        extension = project.extensions.create("versionCheckerOptions")
        project.task("versionChecker") {
            doLast {
                executeVersionChecker(
                    project.rootProject.buildscript.repositories,
                    project.rootProject.buildscript.configurations,
                    "classpath"
                )
                project.allprojects.forEach { p ->
                    val repositories = p.repositories.ifEmpty { project.rootProject.getSettingsRepositories() }
                    executeVersionChecker(repositories, p.configurations, "implementation")
                    executeVersionChecker(repositories, p.configurations, "testImplementation")
                    executeVersionChecker(repositories, p.configurations, "androidTestImplementation")
                }
                executeGradleVersionChecker(project)
                project.extensions.findByName("android")
                    ?.callMemberProperty("composeOptions")
                    ?.callMemberProperty("kotlinCompilerExtensionVersion")
                    ?.let { version: Any ->
                        checkNotNull((version as? String)?.toVersion()) {
                            "Cannot parse compose version: $version"
                        }
                    }
                    ?.let { version: Version ->
                        executeComposeVersionChecker(version)
                    }
            }
        }
    }

    private fun executeGradleVersionChecker(project: Project) {
        val checker = GradleVersionChecker()
        val current = project.gradle.gradleVersion
        val (max, sum) = checker.getMaxVersion()
        if (current != max) {
            println(
                "gradle wrapper $current => $max, use the following command to upgrade:\n"
                    .plus("./gradlew wrapper --gradle-version $max --distribution-type bin")
                    .plus(" --gradle-distribution-sha256-sum $sum")
            )
        }
    }

    private fun executeComposeVersionChecker(current: Version) {
        val checker = ComposeVersionChecker()
        val max = checker.getMaxVersion()
        if (current != max) {
            println("jetpack compose compiler $current => $max")
        }
    }

    private fun executeVersionChecker(
        repositoryHandler: RepositoryHandler,
        configurationContainer: ConfigurationContainer,
        configurationName: String,
    ) {
        val repositories = repositoryHandler.toList()
        val dependencies = configurationContainer.getDependencies(configurationName)
        val checker = VersionChecker(extension, repositories, dependencies)
        checker.fetchMetadata()
        dependencies.forEach { dependency ->
            val current = dependency.version
            val max = checker.getMaxVersion(dependency).toString()
            if (current != max) {
                val gav = dependency.run { "$group:${name.removePrefix("$group.")}:$version" }
                println("$gav => $max")
            }
        }
    }

    private fun ConfigurationContainer.getDependencies(name: String): List<Dependency> =
        findByName(name)?.allDependencies?.filterNot { it is SelfResolvingDependency }.orEmpty()

}