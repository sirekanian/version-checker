package com.sirekanyan.bump

import com.sirekanyan.bump.model.Version
import com.sirekanyan.bump.model.toVersion
import org.gradle.api.artifacts.Dependency

open class BumpExtension {

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