package org.sirekanyan.versionchecker

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.sirekanyan.versionchecker.model.ArtifactKey
import org.sirekanyan.versionchecker.model.Metadata
import org.sirekanyan.versionchecker.model.Version
import org.sirekanyan.versionchecker.model.createArtifactKey
import java.io.File

class VersionChecker(
    private val extension: VersionCheckerExtension,
    private val repositories: List<ArtifactRepository>,
    dependencies: List<Dependency>,
) {

    private val httpClient = HttpClient()
    private val xmlMapper = XmlMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false).registerKotlinModule()
    private val artifactKeys: Set<ArtifactKey>
    private lateinit var metadataCache: Map<ArtifactKey, String?>

    init {
        artifactKeys = mutableSetOf()
        repositories.forEach { repository ->
            dependencies.forEach { dependency ->
                artifactKeys.add(createArtifactKey(repository, dependency))
            }
        }
    }

    fun fetchMetadata() {
        runBlocking {
            metadataCache = artifactKeys.associateWith { downloadMetadataAsync(it) }
                .mapValues { it.value.await() }
        }
    }

    private fun CoroutineScope.downloadMetadataAsync(key: ArtifactKey): Deferred<String?> =
        async(Dispatchers.IO) {
            if (key.url.startsWith("file:/")) {
                val path = key.url.replace(Regex("^file:/+"), "/")
                listOf("maven-metadata-local.xml", "maven-metadata.xml")
                    .map { File("$path/$it") }
                    .find(File::exists)
                    ?.readText()
            } else {
                val response = httpClient.get("${key.url}/maven-metadata.xml")
                if (response.status == HttpStatusCode.NotFound) {
                    null
                } else {
                    response.bodyAsText()
                }
            }
        }

    fun getMaxVersion(dependency: Dependency): Version =
        repositories.flatMap { getVersions(it, dependency) }.maxOrNull() ?: Version.ZERO

    private fun getVersions(
        repository: ArtifactRepository,
        dependency: Dependency,
    ): List<Version> {
        val response = metadataCache.getValue(createArtifactKey(repository, dependency)) ?: return listOf()
        val metadata = xmlMapper.readValue(response, Metadata::class.java)
        return metadata.getAvailableVersions()
            .filter {
                val lessThanVersion = extension.findLessThanVersion(dependency)
                lessThanVersion == null || it < lessThanVersion
            }
            .filter {
                val atMostVersion = extension.findAtMostVersion(dependency)
                atMostVersion == null || it <= atMostVersion
            }
            .filter {
                val phase = extension.getPhase(dependency)
                it.phase >= phase
            }
    }

}