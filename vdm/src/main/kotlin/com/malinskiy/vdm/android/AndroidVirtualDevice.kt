/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android

import com.android.sdklib.AndroidVersion
import com.android.sdklib.ISystemImage
import com.android.sdklib.internal.avd.AvdInfo
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus
import com.android.sdklib.repository.IdDisplay
import com.malinskiy.vdm.VirtualDevice
import java.io.File

class AndroidVirtualDevice(internal val delegate: AvdInfo) : VirtualDevice {
    val id: String
        get() = delegate.name
    val androidVersion: AndroidVersion
        get() = delegate.androidVersion
    val deviceName: String
        get() = delegate.deviceName
    val abiType: String
        get() = delegate.abiType
    val cpuArch: String
        get() = delegate.cpuArch
    val deviceManufacturer: String
        get() = delegate.deviceManufacturer
    val dataFolderPath: String
        get() = delegate.dataFolderPath
    val displayName: String
        get() = delegate.displayName
    val configFile: File
        get() = delegate.configFile
    val iniFile: File
        get() = delegate.iniFile
    val properties: Map<String, String>
        get() = delegate.properties
    val isPlayStoreAvailable: Boolean
        get() = delegate.hasPlayStore()
    val errorMessage: String?
        get() = delegate.errorMessage
    val status: AvdStatus
        get() = delegate.status
    val systemImage: ISystemImage?
        get() = delegate.systemImage
    val tag: IdDisplay?
        get() = delegate.tag

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AndroidVirtualDevice

        if (delegate.name != other.delegate.name) return false

        return true
    }

    override fun hashCode(): Int {
        return delegate.hashCode()
    }

    override fun toString(): String {
        return "AndroidVirtualDevice(id='$id', androidVersion=$androidVersion, deviceName='$deviceName', abiType='$abiType', cpuArch='$cpuArch', deviceManufacturer='$deviceManufacturer', dataFolderPath='$dataFolderPath', displayName='$displayName', configFile=$configFile, iniFile=$iniFile, properties=$properties, isPlayStoreAvailable=$isPlayStoreAvailable, errorMessage='$errorMessage', status=$status, systemImage=$systemImage, tag=$tag)"
    }
}
