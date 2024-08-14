package org.sirekanyan.versionchecker.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

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
                arrayOf("0.0.0-0.0", null),
                arrayOf("0.0.0-0.0.0", Version.ZERO),
                arrayOf("1", Version(1)),
                arrayOf("1.2", Version(1, 2)),
                arrayOf("1.2.3", Version(1, 2, 3)),
                arrayOf("1.2.3-4", Version(1, 2, 3, 4)),
                arrayOf("1.2.3-4.5", null),
                arrayOf("1.2.3-4.5.6", Version(1, 2, 3, 4, 5, 6)),
            )
    }
}
