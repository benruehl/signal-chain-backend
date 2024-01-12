package com.benruehl.infrastructure.persistence.tables

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Devices : IntIdTable() {
    val title = varchar("title", 128)
    val positionX = float("positionX")
    val positionY = float("positionY")
}

class DeviceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DeviceEntity>(Devices)
    var title by Devices.title
    var positionX by Devices.positionX
    var positionY by Devices.positionY
    val incomingLinks by LinkEntity referrersOn Links.targetDevice
    val outgoingLinks by LinkEntity referrersOn Links.sourceDevice
}