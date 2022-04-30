package com.malinskiy.vdm.device

import com.malinskiy.vdm.device.DeviceConfig
import com.malinskiy.vdm.device.Location

interface VirtualDevice {
    val location: Location
    val config: DeviceConfig
}