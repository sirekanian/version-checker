package org.sirekanyan.versionchecker.model

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.internal.artifacts.repositories.ResolutionAwareRepository
import java.net.URI

fun createArtifactKey(repository: ArtifactRepository, dependency: Dependency): ArtifactKey {
    val repositoryProperties = (repository as ResolutionAwareRepository).descriptor.properties
    val repositoryUrl = (repositoryProperties["URL"] as URI).toASCIIString().removeSuffix("/")
    val groupPath = checkNotNull(dependency.group).replace('.', '/')
    val namePath = dependency.name
    val url = "$repositoryUrl/$groupPath/$namePath/maven-metadata.xml"
    return ArtifactKey(url)
}

data class ArtifactKey(val url: String)