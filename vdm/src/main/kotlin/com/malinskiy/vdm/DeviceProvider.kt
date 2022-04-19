/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm

interface DeviceProvider {
    fun browse()
    fun read()
    fun edit()
    fun add()
    fun delete()
}
