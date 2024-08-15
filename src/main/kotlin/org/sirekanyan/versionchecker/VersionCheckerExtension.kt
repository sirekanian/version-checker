package org.sirekanyan.versionchecker

import org.gradle.api.artifacts.Dependency
import org.sirekanyan.versionchecker.model.Phase
import org.sirekanyan.versionchecker.model.Version
import org.sirekanyan.versionchecker.model.toVersion
import java.io.File

open class VersionCheckerExtension {

    var output: File? = null
    val ALPHA = Phase.ALPHA
    val BETA = Phase.BETA
    val RC = Phase.RC
    private val lessThanMap = mutableMapOf<String, Version>()
    private val atMostMap = mutableMapOf<String, Version>()
    private val phaseMap = mutableMapOf<String, Phase>()

    internal fun findLessThanVersion(dependency: Dependency): Version? {
        val group = dependency.group
        val name = dependency.name
        return lessThanMap["$group:$name"] ?: lessThanMap["$group"]
    }

    internal fun findAtMostVersion(dependency: Dependency): Version? {
        val group = dependency.group
        val name = dependency.name
        return atMostMap["$group:$name"] ?: atMostMap["$group"]
    }

    internal fun getPhase(dependency: Dependency): Phase {
        val group = dependency.group
        val name = dependency.name
        return phaseMap["$group:$name"] ?: phaseMap["$group"] ?: Phase.RELEASE
    }

    infix fun String.lessThan(version: String): String {
        lessThanMap[this] = checkNotNull(version.toVersion()) {
            "Unsupported version format: $version"
        }
        return this
    }

    infix fun String.atMost(version: String): String {
        atMostMap[this] = checkNotNull(version.toVersion()) {
            "Unsupported version format: $version"
        }
        return this
    }

    infix fun String.phase(phase: Phase): String {
        phaseMap[this] = phase
        return this
    }
}
