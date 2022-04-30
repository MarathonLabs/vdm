/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm

import com.malinskiy.vdm.device.DeviceConfig
import com.malinskiy.vdm.device.Location
import com.malinskiy.vdm.device.VirtualDevice

interface VirtualDeviceManager {
    fun browse(): List<VirtualDevice>
    fun read(location: Location): VirtualDevice
    fun update(location: Location, updatedConfig: DeviceConfig): VirtualDevice
    fun add(config: DeviceConfig): VirtualDevice
    fun delete(location: Location)
    fun start(location: Location, options: StartupOptions)
}
