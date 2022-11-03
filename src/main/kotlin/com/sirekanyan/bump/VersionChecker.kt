@file:Suppress("DefaultLocale")

package com.sirekanyan.bump

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.sirekanyan.bump.model.ArtifactKey
import com.sirekanyan.bump.model.Metadata
import com.sirekanyan.bump.model.Version
import com.sirekanyan.bump.model.createArtifactKey
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import java.io.File

class VersionChecker(
    private val extension: BumpExtension,
    private val repositories: List<MavenArtifactRepository>,
    dependencies: List<Dependency>
) {

    private val httpClient = HttpClient()
    private val xmlMapper = XmlMapper().registerKotlinModule()
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
            try {
                val url = Url(key.url)
                if (url.protocol.name == "file") {
                    val file = File(url.fullPath)
                    if (file.exists()) {
                        file.readText()
                    } else {
                        null
                    }
                } else {
                    httpClient.get<String>(url)
                }
            } catch (exception: ClientRequestException) {
                if (exception.response.status.value == 404) {
                    null
                } else {
                    throw exception
                }
            }
        }

    fun getMaxVersion(dependency: Dependency): Version =
        repositories.flatMap { getVersions(it, dependency) }.max() ?: Version.ZERO

    private fun getVersions(
        repository: MavenArtifactRepository,
        dependency: Dependency
    ): List<Version> {
        val response = metadataCache.getValue(createArtifactKey(repository, dependency)) ?: return listOf()
        val metadata = xmlMapper.readValue(response, Metadata::class.java)
        return metadata.getAvailableVersions()
            .filter { version ->
                val max = extension.findMax(dependency)
                max == null || version < max
            }
    }

}