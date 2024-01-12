package com.benruehl.infrastructure.persistence.daos

import com.benruehl.domain.entities.Link
import com.benruehl.domain.repositories.LinkRepository
import com.benruehl.infrastructure.persistence.dbQuery
import com.benruehl.infrastructure.persistence.tables.DeviceEntity
import com.benruehl.infrastructure.persistence.tables.LinkEntity
import com.benruehl.infrastructure.persistence.tables.Links
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LinkDAO : LinkRepository {
    override suspend fun save(link: Link): Link = dbQuery {
        LinkEntity.new("${link.sourceDeviceId}_${link.targetDeviceId}") {
            this.sourceDevice = DeviceEntity[link.sourceDeviceId]
            this.targetDevice = DeviceEntity[link.targetDeviceId]
        }.let {
            link.copy(id = it.id.value)
        }
    }

    override suspend fun delete(linkId: String): Boolean = dbQuery {
        Links.deleteWhere { Links.id eq linkId } > 0
    }
}