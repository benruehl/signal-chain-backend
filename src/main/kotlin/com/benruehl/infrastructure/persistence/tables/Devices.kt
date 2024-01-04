package com.benruehl.infrastructure.persistence.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Devices : IntIdTable() {
    val title = varchar("title", 128)
    val positionX = float("positionX")
    val positionY = float("positionY")
}