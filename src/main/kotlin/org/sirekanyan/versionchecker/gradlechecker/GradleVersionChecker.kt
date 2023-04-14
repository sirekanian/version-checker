package org.sirekanyan.versionchecker.gradlechecker

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.sirekanyan.versionchecker.gradlechecker.model.GradleVersion

private const val CURRENT_URL = "https://services.gradle.org/versions/current"

class GradleVersionChecker {

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    fun getMaxVersion(): String = runBlocking {
        httpClient.get(CURRENT_URL).body<GradleVersion>().version
    }

}