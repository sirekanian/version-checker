package com.sirekanyan.bump.model

fun String.toVersion(): Version? {
    fun Regex.groupValues() = matchEntire(this@toVersion)?.groupValues
    fun List<String>.getInt(index: Int) = this[index].toInt()
    Regex("^(\\d+)$").groupValues()?.run {
        return Version(getInt(1))
    }
    Regex("^(\\d+)\\.(\\d+)$").groupValues()?.run {
        return Version(getInt(1), getInt(2))
    }
    Regex("^(\\d+)\\.(\\d+)\\.(\\d+)$").groupValues()?.run {
        return Version(getInt(1), getInt(2), getInt(3))
    }
    Regex("^(\\d+)\\.(\\d+)\\.(\\d+)-(\\d+)$").groupValues()?.run {
        return Version(getInt(1), getInt(2), getInt(3), getInt(4))
    }
    return null
}

data class Version(
    val major: Int,
    val minor: Int? = null,
    val patch: Int? = null,
    val fix: Int? = null,
) : Comparable<Version> {

    private val comparator: Comparator<Version> =
        compareBy<Version> { it.major }
            .thenBy { it.minor ?: 0 }
            .thenBy { it.patch ?: 0 }
            .thenBy { it.fix ?: 0 }

    override fun compareTo(other: Version): Int =
        comparator.compare(this, other)

    override fun toString(): String =
        when {
            fix == null && patch == null && minor == null -> "$major"
            fix == null && patch == null -> "$major.$minor"
            fix == null -> "$major.$minor.$patch"
            else -> "$major.$minor.$patch-$fix"
        }

    companion object {
        val ZERO = Version(0)
    }

}