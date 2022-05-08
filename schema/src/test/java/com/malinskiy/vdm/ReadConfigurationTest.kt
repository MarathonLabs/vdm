/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm

import ConfigurationParser
import java.io.File
import java.nio.file.Path
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReadConfigurationTest {

    private val schema: Path = File(javaClass.classLoader.getResource("schema/vdm-schema.json").file).toPath()

    @Test
    fun testRead() {
        val conf = "android:\n" +
                "  count: 1\n" +
                "  type: \"emulator\""
        val parse = ConfigurationParser(schema).parse(conf)
        assertEquals(1, parse.android.count)
        assertEquals("emulator", parse.android.type)
    }

    @Test
    fun testValidationError() {
        val conf = "android:\n" +
                "  type: \"emulator\""
        assertThrows<IllegalStateException> {
            ConfigurationParser(schema).parse(conf)
        }
    }
}