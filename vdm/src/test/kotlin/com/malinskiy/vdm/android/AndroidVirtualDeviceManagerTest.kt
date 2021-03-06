/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android

import com.malinskiy.vdm.android.interactor.DiscoverAndroidHomeInteractor
import com.malinskiy.vdm.device.VirtualDevice
import org.junit.jupiter.api.Test
import org.opentest4j.TestAbortedException

class AndroidVirtualDeviceManagerTest {

    private val factory = AndroidVirtualDeviceManagerFactory(DiscoverAndroidHomeInteractor())
    private val manager = factory.create()

    @Test
    fun testBrowse() {
        manager.browse().forEach { println(it) }
    }

    @Test
    fun testRead() {
        val firstDevice: VirtualDevice = manager.browse().firstOrNull() ?: throw TestAbortedException("No avd devices present. Ignoring test")
        val virtualDevice = manager.read(firstDevice.id)
        println(virtualDevice)
    }
}
