package com.benruehl.infrastructure.persistence.tables

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Links : IdTable<String>() {
    private val sourceTargetId = varchar("id", 40).uniqueIndex()
    override val id: Column<EntityID<String>> = sourceTargetId.entityId()
    val sourceDevice = reference("sourceDevice", Devices)
    val targetDevice = reference("targetDevice", Devices)
}

class LinkEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, LinkEntity>(Links)
    var sourceDevice by DeviceEntity referencedOn Links.sourceDevice
    var targetDevice by DeviceEntity referencedOn Links.targetDevice
}