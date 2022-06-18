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
        val conf = File(ReadConfigurationTest::class.java.getResource("/schema/sample-1.yaml").file).readText()
        val parse = ConfigurationParser(schema).parse(conf)
        assertEquals(1, parse.android.size)
        assertEquals("1.0.0", parse.apiVersion)
        assertEquals(1, parse.android.first().count)
        assertEquals("STELLAR_DEVICE", parse.android.first().name)
        assertEquals(Configuration.Spec("android-30", listOf("default"), "x86_64"), parse.android.first().spec)
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
