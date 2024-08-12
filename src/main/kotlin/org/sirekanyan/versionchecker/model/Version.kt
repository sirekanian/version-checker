package org.sirekanyan.versionchecker.model

fun String.toVersion(): Version? {
    fun Regex.groupValues() = matchEntire(this@toVersion)?.groupValues
    fun List<String>.getInt(index: Int) = this[index].toInt()
    Regex("^(\\d+)$").groupValues()?.let {
        return Version(this, it.getInt(1))
    }
    Regex("^(\\d+)\\.(\\d+)$").groupValues()?.let {
        return Version(this, it.getInt(1), it.getInt(2))
    }
    Regex("^(\\d+)\\.(\\d+)\\.(\\d+)$").groupValues()?.let {
        return Version(this, it.getInt(1), it.getInt(2), it.getInt(3))
    }
    Regex("^(\\d+)\\.(\\d+)\\.(\\d+)-(\\d+)$").groupValues()?.let {
        return Version(this, it.getInt(1), it.getInt(2), it.getInt(3), it.getInt(4))
    }
    Regex("^(\\d+)\\.(\\d+)\\.(\\d+)-(\\d+)\\.(\\d+)\\.(\\d+)$").groupValues()?.let {
        return Version(this, it.getInt(1), it.getInt(2), it.getInt(3), it.getInt(4), it.getInt(5), it.getInt(6))
    }
    return null
}

class Version(
    private val rawValue: String,
    private val major: Int,
    private val minor: Int? = null,
    private val patch: Int? = null,
    private val fix1: Int? = null,
    private val fix2: Int? = null,
    private val fix3: Int? = null,
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
        rawValue

    companion object {
        val ZERO = Version("0.0.0", 0)
    }

}