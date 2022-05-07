/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android

import com.android.prefs.AndroidLocationsSingleton
import com.android.sdklib.internal.avd.AvdManager
import com.android.sdklib.repository.AndroidSdkHandler
import com.android.utils.StdLogger
import com.malinskiy.vdm.VirtualDeviceManager
import com.malinskiy.vdm.VirtualDeviceManagerFactory
import com.malinskiy.vdm.android.interactor.DiscoverAndroidHomeInteractor

class AndroidVirtualDeviceManagerFactory(
    private val homeDirectory: DiscoverAndroidHomeInteractor
) : VirtualDeviceManagerFactory {

    override fun create(): VirtualDeviceManager {
        val logger = StdLogger(StdLogger.Level.VERBOSE)
        val androidHome = homeDirectory.execute() ?: throw RuntimeException("Unable to locate Android SDK")
        val sdkHandler = AndroidSdkHandler.getInstance(AndroidLocationsSingleton, androidHome.toPath())
        return AndroidVirtualDeviceManager(
            avdManager = AvdManager.getInstance(sdkHandler, logger)
        )
    }
}