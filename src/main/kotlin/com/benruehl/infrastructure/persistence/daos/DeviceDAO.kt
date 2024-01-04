package com.benruehl.infrastructure.persistence.daos;

import com.benruehl.domain.repositories.DeviceRepository
import com.benruehl.domain.entities.Device
import com.benruehl.infrastructure.persistence.dbQuery
import com.benruehl.infrastructure.persistence.tables.Devices
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class DeviceDAO : DeviceRepository {
    override suspend fun findAll(): List<Device> = dbQuery {
        Devices
            .selectAll()
            .map { it.mapToEntity() }
    }

    override suspend fun find(id: Int): Device? = dbQuery {
        Devices
            .select { Devices.id eq id }
            .map { it.mapToEntity() }
            .singleOrNull()
    }

    override suspend fun save(device: Device): Device = dbQuery {
        val query = when (device.id) {
            null -> Devices.insert {
                it.mapFromEntity(device)
            }
            else -> Devices.replace {
                it.mapFromEntity(device)
                it[id] = device.id
            }
        }

        query.resultedValues?.singleOrNull()?.mapToEntity()
            ?: throw Exception("Insert or Update did not return any value.")
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Devices.deleteWhere { Devices.id eq id } > 0
    }

    private fun ResultRow.mapToEntity() = Device(
        id = this[Devices.id].value,
        title = this[Devices.title],
        positionX = this[Devices.positionX],
        positionY = this[Devices.positionY],
    )

    private fun UpdateBuilder<*>.mapFromEntity(device: Device) {
        this[Devices.title] = device.title
        this[Devices.positionX] = device.positionX
        this[Devices.positionY] = device.positionY
    }
}
