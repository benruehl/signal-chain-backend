package com.benruehl.application.services

import com.benruehl.application.dtos.CreateLinkRequest
import com.benruehl.application.dtos.QueryLinkResponse
import com.benruehl.domain.entities.Link
import com.benruehl.infrastructure.persistence.daos.LinkDAO
import kotlin.Exception

class LinkService {
    private val linkRepository = LinkDAO()

    suspend fun create(link: CreateLinkRequest): QueryLinkResponse {
        if (link.sourceDeviceId == link.targetDeviceId) {
            throw Exception("Links cannot be self-referencing. [deviceId=${link.sourceDeviceId}]")
        }

        val createdLink = linkRepository.save(
            Link(
                id = null,
                sourceDeviceId = link.sourceDeviceId,
                targetDeviceId = link.targetDeviceId
            )
        )
        return QueryLinkResponse(
            id = createdLink.id ?: throw Exception("Attempted to return an entity without id to client!"),
            sourceDeviceId = link.sourceDeviceId,
            targetDeviceId = link.targetDeviceId
        )
    }

    suspend fun delete(id: String): Boolean {
        return linkRepository.delete(id)
    }
}