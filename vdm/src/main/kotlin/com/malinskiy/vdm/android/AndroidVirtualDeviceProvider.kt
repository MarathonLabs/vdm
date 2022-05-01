/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android

import com.android.prefs.AndroidLocationsSingleton
import com.android.sdklib.internal.avd.AvdManager
import com.android.sdklib.repository.AndroidSdkHandler
import com.android.utils.ILogger
import com.android.utils.StdLogger
import com.malinskiy.vdm.VirtualDeviceProvider
import com.malinskiy.vdm.android.interactor.DiscoverHomeInteractor
import java.io.File

class AndroidVirtualDeviceProvider : VirtualDeviceProvider<AndroidVirtualDevice> {
    private val avdManager: AvdManager by lazy {
        val androidHome: File =
            DiscoverHomeInteractor().execute() ?: throw RuntimeException("Unable to locate Android SDK")
        val sdkHandler = AndroidSdkHandler.getInstance(AndroidLocationsSingleton, androidHome.toPath())
        AvdManager.getInstance(sdkHandler, logger)
    }
    private val logger: ILogger by lazy { StdLogger(StdLogger.Level.VERBOSE) }

    override fun browse(): List<AndroidVirtualDevice> {
        return avdManager.allAvds.map {
            AndroidVirtualDevice(it)
        }
    }

    override fun read(id: String): AndroidVirtualDevice? {
        return AndroidVirtualDevice(avdManager.getAvd(id, false))
    }

    override fun edit(block: (AndroidVirtualDevice) -> Unit): Boolean {
        TODO("Not yet implemented")
    }

    override fun add(): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(device: AndroidVirtualDevice): Boolean {
        return avdManager.deleteAvd(device.delegate, logger)
    }

    override fun start(device: AndroidVirtualDevice): Boolean {
        TODO("Not yet implemented")
    }

    override fun stop(device: AndroidVirtualDevice): Boolean {
        avdManager.stopAvd(device.delegate)
        return true
    }
}
