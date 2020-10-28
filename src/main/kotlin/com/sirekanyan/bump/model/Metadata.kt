package com.sirekanyan.bump.model

data class Metadata(
    val groupId: String,
    val artifactId: String,
    val version: String?,
    val versioning: Versioning
) {

    fun getAvailableVersions(): List<Version> =
        versioning.versions
            .plus(version)
            .plus(versioning.latest)
            .plus(versioning.release)
            .mapNotNull { it?.toVersion() }

    data class Versioning(
        val latest: String,
        val release: String?,
        val versions: List<String>,
        val lastUpdated: String
    )

}