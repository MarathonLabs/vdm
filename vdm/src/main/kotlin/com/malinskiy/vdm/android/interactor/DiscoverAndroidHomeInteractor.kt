/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android.interactor

import java.io.File

class DiscoverAndroidHomeInteractor {
    fun execute(): File? = try {
        System.getenv("ANDROID_HOME") ?: System.getenv("ANDROID_SDK_ROOT")
    } catch (e: SecurityException) {
        null
    }?.let { File(it) }
}
