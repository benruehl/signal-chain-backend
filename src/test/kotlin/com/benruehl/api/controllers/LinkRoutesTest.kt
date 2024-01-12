package com.benruehl.api.controllers

import com.benruehl.*
import com.benruehl.application.dtos.QueryLinkResponse
import com.benruehl.infrastructure.persistence.daos.DeviceDAO
import com.benruehl.infrastructure.persistence.daos.LinkDAO
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class LinkRoutesTest {

    private val deviceRepository = DeviceDAO()
    private val linkRepository = LinkDAO()

    @Test
    fun `POST link should return link with id`() = setupApplication({
        deviceRepository.save(aDevice)
        deviceRepository.save(anotherDevice)
    }) { client ->
        client.post("/links") {
            setBody(aLink(aDevice, anotherDevice).toRequestBody())
            contentType(Json)
        }.apply {
            val response = bodyDeserializedAs<QueryLinkResponse>()
            assertEquals(HttpStatusCode.Created, status)
            assertEquals("${aDevice.id}_${anotherDevice.id}", response.id)
        }
    }

    @Test
    fun `POST link should save link to database`() = setupApplication({
        deviceRepository.save(aDevice)
        deviceRepository.save(anotherDevice)
    }) { client ->
        val link = aLink(aDevice, anotherDevice)
        client.post("/links") {
            setBody(link.toRequestBody())
            contentType(Json)
        }.apply {
            assertEquals(link, deviceRepository.find(aDevice.id!!)?.outgoingLink)
        }
    }

    @Test
    fun `DELETE link should return ok when link exists`() = setupApplication({
        deviceRepository.save(aDevice)
        deviceRepository.save(anotherDevice)
        linkRepository.save(aLink(aDevice, anotherDevice))
    }) {
        client.delete("/links/${aLink(aDevice, anotherDevice).id}").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun `DELETE link should return NotFound when link does not exist`() = setupApplication {
        client.delete("/links/${aLink(aDevice, anotherDevice).id}").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `DELETE link should remove link from database`() = setupApplication({
        deviceRepository.save(aDevice)
        deviceRepository.save(anotherDevice)
        linkRepository.save(aLink(aDevice, anotherDevice))
    }) {
        client.delete("/links/${aLink(aDevice, anotherDevice).id}").apply {
            assertEquals(0, deviceRepository.findAll().mapNotNull { it.outgoingLink }.size)
        }
    }
}