/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm

interface VirtualDeviceProvider<T : VirtualDevice> {
    fun browse(): List<T>
    fun read(id: String): T?
    fun edit(block: (T) -> Unit): Boolean
    fun add(): Boolean
    fun delete(device: T): Boolean
    fun start(device: T): Boolean
    fun stop(device: T): Boolean
}
