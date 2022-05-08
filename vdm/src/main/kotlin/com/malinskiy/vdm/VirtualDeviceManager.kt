/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm

import com.malinskiy.vdm.device.Config
import com.malinskiy.vdm.device.Id
import com.malinskiy.vdm.device.VirtualDevice

interface VirtualDeviceManager {
    fun browse(): List<VirtualDevice>
    fun read(id: Id): VirtualDevice
    fun update(id: Id, updatedConfig: Config): VirtualDevice
    fun add(config: Config): VirtualDevice
    fun delete(id: Id)
    fun start(id: Id, options: StartupOptions)
    fun stop(device: VirtualDevice)
}
