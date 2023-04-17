package org.sirekanyan.versionchecker.composechecker

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import org.sirekanyan.versionchecker.model.Metadata
import org.sirekanyan.versionchecker.model.Version

private const val METADATA_URL =
    "https://dl.google.com/android/maven2/androidx/compose/compiler/compiler/maven-metadata.xml"

class ComposeVersionChecker {

    private val httpClient = HttpClient()
    private val xmlMapper = XmlMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false).registerKotlinModule()

    fun getMaxVersion(): Version =
        getVersions().maxOrNull() ?: Version.ZERO

    private fun getVersions(): List<Version> {
        val content = runBlocking { httpClient.get(METADATA_URL).bodyAsText() }
        val metadata = xmlMapper.readValue(content, Metadata::class.java)
        return metadata.getAvailableVersions()
    }

}