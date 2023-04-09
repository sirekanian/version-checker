package com.sirekanyan.bump

import com.sirekanyan.bump.extensions.getSettingsRepositories
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.create

class BumpPlugin : Plugin<Project> {

    private lateinit var extension: BumpExtension

    override fun apply(project: Project) {
        extension = project.extensions.create("bump")
        project.task("bump") {
            doLast {
                bump(
                    project.rootProject.buildscript.repositories,
                    project.rootProject.buildscript.configurations,
                    "classpath"
                )
                project.allprojects.forEach { p ->
                    bump(
                        p.repositories.ifEmpty { project.rootProject.getSettingsRepositories() },
                        p.configurations,
                        "implementation"
                    )
                }
            }
        }
    }

    private fun bump(
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
                val gav = dependency.run { "$group:$name:$version" }
                println("$gav => $max")
            }
        }
    }

    private fun ConfigurationContainer.getDependencies(name: String): List<Dependency> =
        getByName(name).allDependencies.toList()

}