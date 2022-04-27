/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android

import org.junit.jupiter.api.Test

class AndroidVirtualDeviceProviderTest {
    
    private val deviceProvider = AndroidVirtualDeviceProvider()
    
    
    @Test
    fun testBrowse() {
        deviceProvider.browse().forEach { println(it) }
    }
    
    @Test
    fun testRead() {
        val firstDevice = deviceProvider.browse().first()
        val virtualDevice = deviceProvider.read(firstDevice.id)
        println(virtualDevice)
    }
}
