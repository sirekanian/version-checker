package org.sirekanyan.versionchecker

import org.gradle.api.artifacts.Dependency
import org.sirekanyan.versionchecker.model.Version
import org.sirekanyan.versionchecker.model.toVersion

open class VersionCheckerExtension {

    private val lessThanMap = mutableMapOf<String, Version>()
    private val atMostMap = mutableMapOf<String, Version>()

    fun findLessThanVersion(dependency: Dependency): Version? {
        val group = dependency.group
        val name = dependency.name
        return lessThanMap["$group:$name"] ?: lessThanMap["$group"]
    }

    fun findAtMostVersion(dependency: Dependency): Version? {
        val group = dependency.group
        val name = dependency.name
        return atMostMap["$group:$name"] ?: atMostMap["$group"]
    }

    infix fun String.lessThan(version: String) {
        lessThanMap[this] = checkNotNull(version.toVersion()) {
            "Unsupported version format: $version"
        }
    }

    infix fun String.atMost(version: String) {
        atMostMap[this] = checkNotNull(version.toVersion()) {
            "Unsupported version format: $version"
        }
    }

}