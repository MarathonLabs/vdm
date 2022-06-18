/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm.android

import com.android.prefs.AndroidLocationsException
import com.android.repository.api.ConsoleProgressIndicator
import com.android.sdklib.devices.Device
import com.android.sdklib.devices.DeviceManager
import com.android.sdklib.internal.avd.AvdInfo
import com.android.sdklib.internal.avd.AvdManager
import com.android.sdklib.internal.avd.EmulatedProperties
import com.android.sdklib.internal.avd.HardwareProperties
import com.android.sdklib.repository.AndroidSdkHandler
import com.android.sdklib.repository.meta.DetailsTypes
import com.android.sdklib.repository.targets.SystemImage
import com.android.sdklib.tool.AvdManagerCli
import com.android.utils.ILogger
import com.google.common.base.Joiner
import com.malinskiy.vdm.StartupOptions
import com.malinskiy.vdm.VirtualDeviceManager
import com.malinskiy.vdm.device.Config
import com.malinskiy.vdm.device.Id
import com.malinskiy.vdm.device.VirtualDevice
import com.malinskiy.vdm.device.android.AndroidVirtualDevice
import com.malinskiy.vdm.device.android.AvdConfig
import java.io.IOException
import java.nio.file.Path
import java.util.*
import kotlin.RuntimeException

class AndroidVirtualDeviceManager(
    private val sdkHandler: AndroidSdkHandler,
    private val logger: ILogger
) : VirtualDeviceManager {
    private val avdManager: AvdManager = AvdManager.getInstance(sdkHandler, logger)

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

    override fun add(config: Config, override: Boolean): VirtualDevice {
        val progressIndicator = ConsoleProgressIndicator()
        val sysImgs = sdkHandler.getSystemImageManager(progressIndicator).imageMap

        if (sysImgs.isEmpty) {
            throw RuntimeException("No system images available")
        }

        try {
            val avdName: String = config
            if (!AvdManagerCli.RE_AVD_NAME.matcher(avdName).matches()) {
                errorAndExit(
                    "AVD name '%1\$s' contains invalid characters.\nAllowed characters are: %2\$s",
                    avdName, AvdManagerCli.CHARS_AVD_NAME
                )
                return
            }
            val info = avdManager.getAvd(avdName, false /*validAvdOnly*/)
            if (info != null) {
                if (override) {
                    mSdkLog.warning(
                        "Android Virtual Device '%s' already exists and will be replaced.",
                        avdName
                    )
                } else {
                    errorAndExit(
                        """
                    Android Virtual Device '%s' already exists.
                    Use --force if you want to replace it.
                    """.trimIndent(),
                        avdName
                    )
                    return
                }
            }
            val paramFolderPath: String = getParamLocationPath()
            val avdFolder: Path
            avdFolder = if (paramFolderPath != null) {
                mSdkHandler.getFileOp().toPath(paramFolderPath)
            } else {
                AvdInfo.getDefaultAvdFolder(avdManager, avdName, false)
            }
            var tag = SystemImage.DEFAULT_TAG
            var abiType: String? = getParamAbi()
            var cmdTag: String? = getParamTag()
            if (cmdTag == null) {
                val details = imagePkg!!.typeDetails as DetailsTypes.SysImgDetailsType
                // TODO: support multi-tag
                val tags = details.tags
                tag = if (tags.isEmpty()) null else tags[0]
            }
            if (abiType != null && abiType.indexOf('/') != -1) {
                val segments = abiType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (segments.size == 2) {
                    if (cmdTag == null) {
                        cmdTag = segments[0]
                    } else if (cmdTag != segments[0]) {
                        errorAndExit(
                            "--%1\$s %2\$s conflicts with --%3\$s %4\$s.",
                            AvdManagerCli.KEY_TAG,
                            cmdTag,
                            AvdManagerCli.KEY_ABI,
                            abiType
                        )
                    }
                    abiType = segments[1]
                } else {
                    errorAndExit(
                        "Invalid --%1\$s %2\$s: expected format 'abi' or 'tag/abi'.",
                        AvdManagerCli.KEY_ABI,
                        abiType
                    )
                }
            }

            // if no tag was specified, "default" is implied
            if (cmdTag == null || cmdTag.isEmpty()) {
                cmdTag = tag!!.id
            }

            // Collect all possible tags for the selected target
            val tags: MutableSet<String?> = HashSet()
            for (systemImage in sysImgs) {
                tags.add(systemImage.tag.id)
            }
            if (!tags.contains(cmdTag)) {
                errorAndExit(
                    "Invalid --%1\$s %2\$s for the selected package. Valid tags are:\n%3\$s",
                    AvdManagerCli.KEY_TAG, cmdTag, Joiner.on("\n").join(tags)
                )
            }
            var img: SystemImage? = null
            if (abiType == null || abiType.isEmpty()) {
                if (sysImgs.size == 1) {
                    // Auto-select the single ABI available
                    abiType = sysImgs.iterator().next().abiType
                    img = sysImgs.iterator().next()
                    mSdkLog.info("Auto-selecting single ABI %1\$s\n", abiType)
                } else {
                    displayTagAbiList(sysImgs, "Valid ABIs: ")
                    errorAndExit(
                        "This package has more than one ABI. Please specify one using --%1\$s.",
                        AvdManagerCli.KEY_ABI
                    )
                }
            } else {
                for (systemImage in sysImgs) {
                    if (systemImage.abiType == abiType) {
                        img = systemImage
                        break
                    }
                }
                if (img == null) {
                    displayTagAbiList(sysImgs, "Valid ABIs: ")
                    errorAndExit(
                        "Invalid --%1\$s %2\$s for the selected package.",
                        AvdManagerCli.KEY_ABI,
                        abiType
                    )
                }
            }
            assert(img != null)
            var device: Device? = null
            val deviceParam: String = getParamDevice()
            if (deviceParam != null) {
                val devices: List<Device> = ArrayList<Device>(
                    createDeviceManager().getDevices(DeviceManager.ALL_DEVICES)
                )
                Collections.sort(devices, Device.getDisplayComparator())
                var index = -1
                try {
                    index = deviceParam.toInt()
                } catch (ignore: NumberFormatException) {
                }
                if (index >= 0 && index < devices.size) {
                    device = devices[index]
                } else {
                    for (d in devices) {
                        if (deviceParam == d.id) {
                            device = d
                            break
                        }
                    }
                }
                if (device == null) {
                    errorAndExit(
                        "No device found matching --%1\$s %2\$s.",
                        AvdManagerCli.KEY_DEVICE,
                        deviceParam
                    )
                }
            }
            var hardwareConfig: MutableMap<String?, String?> = TreeMap()
            if (device != null) {
                // The user selected a hardware configuration. Don't ask if they
                // want custom hardware.
                // Start with the default values, then overlay the selected hardware.
                hardwareConfig = defaultHardwareConfig()
                hardwareConfig.putAll(DeviceManager.getHardwareProperties(device))
                EmulatedProperties.restrictDefaultRamSize(hardwareConfig)
            } else {
                try {
                    // Take the generic hardware config, possibly customized by the user
                    hardwareConfig = promptForHardware()
                } catch (e: IOException) {
                    errorAndExit(e.message)
                }
            }
            if (getParamSdCard() != null) {
                hardwareConfig[HardwareProperties.HW_SDCARD] = HardwareProperties.BOOLEAN_YES
            }
            AvdManagerCli.updateUninitializedDynamicParameters(hardwareConfig)
            val newAvdInfo// newAvdInfo is never read, yet useful for debugging
                    = avdManager.createAvd(
                avdFolder,
                avdName,
                img,
                null,
                null,
                getParamSdCard(),
                hardwareConfig,
                device?.bootProps,
                false,
                override,
                false,
                mSdkLog
            )
            if (newAvdInfo == null) {
                errorAndExit("AVD not created.")
            }
        } catch (e: AndroidLocationsException) {
            errorAndExit(e.message)
        }
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
