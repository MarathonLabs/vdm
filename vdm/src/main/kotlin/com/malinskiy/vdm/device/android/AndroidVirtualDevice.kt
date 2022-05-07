/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.device.android

import com.malinskiy.vdm.device.Config
import com.malinskiy.vdm.device.Id
import com.malinskiy.vdm.device.VirtualDevice

class AndroidVirtualDevice(
    override val config: Config,
) : VirtualDevice {

    override val id: Id = AvdId(config.info.name)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AndroidVirtualDevice

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return config.info.hashCode()
    }

    override fun toString(): String {
        return "AndroidVirtualDevice(config=$config)"
    }
}