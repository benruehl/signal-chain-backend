package com.benruehl.domain.repositories

import com.benruehl.domain.entities.Device

interface DeviceRepository {
    suspend fun findAll(): List<Device>
    suspend fun find(id: Int): Device?
    suspend fun save(device: Device): Device
    suspend fun delete(id: Int): Boolean
}