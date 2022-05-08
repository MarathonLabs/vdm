/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android

import com.android.sdklib.internal.avd.AvdManager
import com.malinskiy.vdm.StartupOptions
import com.malinskiy.vdm.VirtualDeviceManager
import com.malinskiy.vdm.device.Config
import com.malinskiy.vdm.device.Id
import com.malinskiy.vdm.device.VirtualDevice
import com.malinskiy.vdm.device.android.AndroidVirtualDevice
import com.malinskiy.vdm.device.android.AvdConfig

class AndroidVirtualDeviceManager(
    private val avdManager: AvdManager,
) : VirtualDeviceManager {

    override fun browse(): List<VirtualDevice> {
        return avdManager.allAvds.map {
            AndroidVirtualDevice(AvdConfig(it))
        }
    }

    override fun read(id: Id): VirtualDevice {
        return AndroidVirtualDevice(AvdConfig(avdManager.getAvd(id.name, false)))
    }

    override fun update(id: Id, updatedConfig: Config): VirtualDevice {
        TODO("Not yet implemented")
    }

    override fun add(config: Config): VirtualDevice {
        TODO("Not yet implemented")
    }

    override fun delete(id: Id) {
        TODO("Not yet implemented")
    }

    override fun start(id: Id, options: StartupOptions) {
        TODO("Not yet implemented")
    }

    override fun stop(device: VirtualDevice) {
        avdManager.stopAvd(device.config.info)
    }
}
