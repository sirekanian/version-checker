package org.sirekanyan.versionchecker.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import org.sirekanyan.versionchecker.model.Phase.*

@RunWith(Parameterized::class)
class VersionTest(private val input: String, private val output: Version?) {

    @Test
    fun toVersionTest() {
        if (output == null) {
            assertEquals(null, input.toVersion())
        } else {
            assertEquals(0, input.toVersion()?.compareTo(output))
        }
    }

    companion object {
        @JvmStatic
        @Parameters(name = "{0}")
        fun data(): List<Array<*>> =
            listOf(
                arrayOf("", null),
                arrayOf("0", Version.ZERO),
                arrayOf("0.0", Version.ZERO),
                arrayOf("0.0.0", Version.ZERO),
                arrayOf("0.0.0-0", Version.ZERO),
                arrayOf("0.0.0-0.0", Version.ZERO),
                arrayOf("0.0.0-0.0.0", Version.ZERO),
                arrayOf("1", Version("", 1)),
                arrayOf("1.2", Version("", 1, 2)),
                arrayOf("1.2.3", Version("", 1, 2, 3)),
                arrayOf("1.2.3-4", Version("", 1, 2, 3, 4)),
                arrayOf("1.2.3-4.5", Version("", 1, 2, 3, 4, 5)),
                arrayOf("1.2.3-4.5.6", Version("", 1, 2, 3, 4, 5, 6)),
                arrayOf("1-alpha", Version("", 1, 0, 0, 0, 0, 0, ALPHA)),
                arrayOf("1.Beta-0", Version("", 1, 0, 0, 0, 0, 0, BETA)),
                arrayOf("1.2-RC1", Version("", 1, 2, 0, 0, 0, 0, RC, 1)),
                arrayOf("1.2.release-2", null),
                arrayOf("1.2.3.Alpha2", Version("", 1, 2, 3, 0, 0, 0, ALPHA, 2)),
                arrayOf("1.2.3-BETA-1", Version("", 1, 2, 3, 0, 0, 0, BETA, 1)),
                arrayOf("1.2.3-4.rc0", Version("", 1, 2, 3, 4, 0, 0, RC)),
                arrayOf("1.2.3-4-Release", null),
                arrayOf("1.2.3-4.5-ALPHA", Version("", 1, 2, 3, 4, 5, 0, ALPHA)),
                arrayOf("1.2.3-4.5.beta-0", Version("", 1, 2, 3, 4, 5, 0, BETA)),
                arrayOf("1.2.3-4.5.6-Rc1", Version("", 1, 2, 3, 4, 5, 6, RC, 1)),
                arrayOf("1.2.3-4.5.6.RELEASE-2", null),
            )
    }
}
