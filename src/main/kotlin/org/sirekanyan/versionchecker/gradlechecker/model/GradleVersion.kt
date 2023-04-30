package org.sirekanyan.versionchecker.gradlechecker.model

import kotlinx.serialization.Serializable

@Serializable
class GradleVersion(val version: String, val checksumUrl: String)