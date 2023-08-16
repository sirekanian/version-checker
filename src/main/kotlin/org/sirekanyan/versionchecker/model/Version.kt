package org.sirekanyan.versionchecker.model

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
    Regex("^(\\d+)\\.(\\d+)\\.(\\d+)-(\\d+)\\.(\\d+)\\.(\\d+)$").groupValues()?.run {
        return Version(getInt(1), getInt(2), getInt(3), getInt(4), getInt(5), getInt(6))
    }
    return null
}

data class Version(
    val major: Int,
    val minor: Int? = null,
    val patch: Int? = null,
    val fix1: Int? = null,
    val fix2: Int? = null,
    val fix3: Int? = null,
) : Comparable<Version> {

    private val comparator: Comparator<Version> =
        compareBy<Version> { it.major }
            .thenBy { it.minor ?: 0 }
            .thenBy { it.patch ?: 0 }
            .thenBy { it.fix1 ?: 0 }
            .thenBy { it.fix2 ?: 0 }
            .thenBy { it.fix3 ?: 0 }

    override fun compareTo(other: Version): Int =
        comparator.compare(this, other)

    override fun toString(): String =
        when {
            fix3 == null && fix2 == null && fix1 == null && patch == null && minor == null -> "$major"
            fix3 == null && fix2 == null && fix1 == null && patch == null -> "$major.$minor"
            fix3 == null && fix2 == null && fix1 == null -> "$major.$minor.$patch"
            fix3 == null && fix2 == null -> "$major.$minor.$patch-$fix1"
            else -> "$major.$minor.$patch-$fix1.$fix2.$fix3"
        }

    companion object {
        val ZERO = Version(0)
    }

}