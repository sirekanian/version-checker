package org.sirekanyan.versionchecker

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.SelfResolvingDependency
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.create
import org.sirekanyan.versionchecker.extensions.getSettingsRepositories

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
                    executeVersionChecker(
                        p.repositories.ifEmpty { project.rootProject.getSettingsRepositories() },
                        p.configurations,
                        "implementation"
                    )
                }
            }
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
        getByName(name).allDependencies.filterNot { it is SelfResolvingDependency }

}