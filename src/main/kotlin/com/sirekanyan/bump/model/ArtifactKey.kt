package com.sirekanyan.bump.model

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

fun createArtifactKey(repository: ArtifactRepository, dependency: Dependency): ArtifactKey {
    val repositoryUrl = (repository as MavenArtifactRepository).url.toASCIIString().removeSuffix("/")
    val groupPath = checkNotNull(dependency.group).replace('.', '/')
    val namePath = dependency.name.replace('.', '/')
    val url = "$repositoryUrl/$groupPath/$namePath/maven-metadata.xml"
    return ArtifactKey(url)
}

data class ArtifactKey(val url: String)