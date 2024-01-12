package com.benruehl.application.services

import com.benruehl.application.dtos.QueryDeviceResponse
import com.benruehl.application.dtos.QueryLinkResponse
import com.benruehl.application.dtos.SaveDeviceRequest
import com.benruehl.domain.entities.Device
import com.benruehl.domain.entities.Link
import com.benruehl.infrastructure.persistence.daos.DeviceDAO
import java.lang.Exception

class DeviceService {
    private val deviceRepository = DeviceDAO()

    suspend fun findAll(): List<QueryDeviceResponse> {
        return deviceRepository.findAll().map { it.mapToDto() }
    }

    suspend fun find(id: Int): QueryDeviceResponse? {
        return deviceRepository.find(id)?.mapToDto()
    }

    suspend fun create(device: SaveDeviceRequest): QueryDeviceResponse {
        val entity = device.mapToEntity()
        val createdEntity = deviceRepository.save(entity)
        return createdEntity.mapToDto()
    }

    suspend fun update(id: Int, device: SaveDeviceRequest): QueryDeviceResponse {
        val entity = device.mapToEntity().copy(id = id)
        val updatedEntity = deviceRepository.save(entity)
        return updatedEntity.mapToDto()
    }

    suspend fun delete(id: Int): Boolean {
        return deviceRepository.delete(id)
    }

    private fun Device.mapToDto() = this.let {
        QueryDeviceResponse(
            id = it.id ?: throw Exception("Attempted to return an entity without id to client!"),
            title = it.title,
            positionX = it.positionX,
            positionY = it.positionY,
            incomingLinks = it.incomingLinks.map { l -> l.mapToDto() },
            outgoingLink = it.outgoingLink?.mapToDto()
        )
    }

    private fun Link.mapToDto() = this.let {
        QueryLinkResponse(
            id = it.id ?: throw Exception("Attempted to return an entity without id to client!"),
            sourceDeviceId = it.sourceDeviceId,
            targetDeviceId = it.targetDeviceId
        )
    }

    private fun SaveDeviceRequest.mapToEntity() = this.let {
        Device(
            id = null,
            title = it.title,
            positionX = it.positionX,
            positionY = it.positionY,
            incomingLinks = emptyList(),
            outgoingLink = null
        )
    }
}