package org.sirekanyan.versionchecker.model

import org.sirekanyan.versionchecker.extensions.intValue

private val VersionRegex: Regex =
    Regex("""^(\d+)(\.(\d+))?(\.(\d+))?(-(\d+))?(\.(\d+))?(\.(\d+))?([-.]([A-Za-z]+)(-?(\d+))?)?$""")

fun String.toVersion(): Version? =
    VersionRegex.matchEntire(this)?.groups?.let { groups ->
        Phase.of(groups[13]?.value)?.let { phase ->
            Version(
                rawValue = this,
                major = groups[1].intValue,
                minor = groups[3].intValue,
                patch = groups[5].intValue,
                fix1 = groups[7].intValue,
                fix2 = groups[9].intValue,
                fix3 = groups[11].intValue,
                phase = phase,
                iteration = groups[15].intValue,
            )
        }
    }

class Version(
    private val rawValue: String,
    private val major: Int,
    private val minor: Int = 0,
    private val patch: Int = 0,
    private val fix1: Int = 0,
    private val fix2: Int = 0,
    private val fix3: Int = 0,
    val phase: Phase = Phase.RELEASE,
    private val iteration: Int = 0,
) : Comparable<Version> {

    private val comparator: Comparator<Version> =
        compareBy<Version> { it.major }
            .thenBy { it.minor }
            .thenBy { it.patch }
            .thenBy { it.fix1 }
            .thenBy { it.fix2 }
            .thenBy { it.fix3 }
            .thenBy { it.phase }
            .thenBy { it.iteration }

    override fun compareTo(other: Version): Int =
        comparator.compare(this, other)

    override fun equals(other: Any?): Boolean =
        when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> rawValue == (other as Version).rawValue
        }

    override fun hashCode(): Int =
        rawValue.hashCode()

    override fun toString(): String =
        rawValue

    companion object {
        val ZERO = Version("0.0.0", 0)
    }
}
