/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.device.android

import com.android.sdklib.internal.avd.AvdInfo
import com.malinskiy.vdm.device.Config

class ConfigImpl(override val info: AvdInfo) : Config {

    override fun toString(): String {
        return "Config(${info.toDebugString()})"
    }
}