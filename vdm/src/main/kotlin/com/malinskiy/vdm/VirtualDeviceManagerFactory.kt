/*
 * SPDX-License-Identifier: Apache-2.0
 */

package com.malinskiy.vdm

interface VirtualDeviceManagerFactory {
    fun create(): VirtualDeviceManager
}