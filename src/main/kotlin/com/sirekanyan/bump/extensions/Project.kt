package com.sirekanyan.bump.extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal

@Suppress("UnstableApiUsage")
fun Project.getSettingsRepositories(): RepositoryHandler =
    settings.dependencyResolutionManagement.repositories

/**
 * https://github.com/gradle/gradle/issues/17295
 */
private val Project.settings: Settings
    get() = (gradle as GradleInternal).settings
