/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android

import com.malinskiy.vdm.android.interactor.DiscoverAndroidHomeInteractor
import org.junit.jupiter.api.Test

class AndroidVirtualDeviceManagerTest {

    private val factory = AndroidVirtualDeviceManagerFactory(DiscoverAndroidHomeInteractor())
    private val manager = factory.create()

    @Test
    fun testBrowse() {
        manager.browse().forEach { println(it) }
    }

    @Test
    fun testRead() {
        val firstDevice = manager.browse().first()
        val virtualDevice = manager.read(firstDevice.id)
        println(virtualDevice)
    }
}
