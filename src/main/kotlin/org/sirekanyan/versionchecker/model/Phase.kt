package org.sirekanyan.versionchecker.model

enum class Phase(private val key: String?) {

    ALPHA("alpha"),
    BETA("beta"),
    RC("rc"),
    RELEASE(null);

    companion object {
        fun of(key: String): Phase? =
            values().find { it.key.equals(key, ignoreCase = true) }
    }
}
