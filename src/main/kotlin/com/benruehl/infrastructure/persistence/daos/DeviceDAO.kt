package com.benruehl.infrastructure.persistence.daos

import com.benruehl.domain.repositories.DeviceRepository
import com.benruehl.domain.entities.Device
import com.benruehl.domain.entities.Link
import com.benruehl.infrastructure.persistence.dbQuery
import com.benruehl.infrastructure.persistence.tables.DeviceEntity
import com.benruehl.infrastructure.persistence.tables.Devices
import com.benruehl.infrastructure.persistence.tables.LinkEntity
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class DeviceDAO : DeviceRepository {
    override suspend fun findAll(): List<Device> = dbQuery {
        DeviceEntity
            .all()
            .with(DeviceEntity::incomingLinks)
            .map { it.mapToDomainEntity() }
    }

    override suspend fun find(id: Int): Device? = dbQuery {
        DeviceEntity
            .findById(id)
            ?.mapToDomainEntity()
    }

    override suspend fun save(device: Device): Device = dbQuery {
        val query = when (device.id) {
            null -> Devices.insert {
                it.mapFromDomainEntity(device)
            }
            else -> Devices.replace {
                it.mapFromDomainEntity(device)
                it[id] = device.id
            }
        }

        query.resultedValues?.singleOrNull()?.let { DeviceEntity.wrapRow(it) }?.mapToDomainEntity()
            ?: throw Exception("Insert or Update did not return any value.")
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Devices.deleteWhere { Devices.id eq id } > 0
    }

    private fun DeviceEntity.mapToDomainEntity() = Device(
        id = this.id.value,
        title = this.title,
        positionX = this.positionX,
        positionY = this.positionY,
        incomingLinks = this.incomingLinks.map { it.mapToDomainEntity() },
        outgoingLink = this.outgoingLinks.singleOrNull().let { it?.mapToDomainEntity() }
    )

    private fun LinkEntity.mapToDomainEntity() = Link(
        id = this.id.value,
        sourceDeviceId = this.sourceDevice.id.value,
        targetDeviceId = this.targetDevice.id.value
    )

    private fun UpdateBuilder<*>.mapFromDomainEntity(device: Device) {
        this[Devices.title] = device.title
        this[Devices.positionX] = device.positionX
        this[Devices.positionY] = device.positionY
    }
}
