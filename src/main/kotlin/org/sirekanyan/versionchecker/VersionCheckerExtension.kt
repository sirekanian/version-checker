package org.sirekanyan.versionchecker

import org.gradle.api.artifacts.Dependency
import org.sirekanyan.versionchecker.model.Version
import org.sirekanyan.versionchecker.model.toVersion

open class VersionCheckerExtension {

    private val maxMap = mutableMapOf<String, Version>()

    fun findMax(dependency: Dependency): Version? {
        val group = dependency.group
        val name = dependency.name
        return maxMap["$group:$name"] ?: maxMap["$group"]
    }

    infix fun String.lessThan(version: String) {
        maxMap[this] = checkNotNull(version.toVersion()) {
            "Unsupported version format: $version"
        }
    }

}