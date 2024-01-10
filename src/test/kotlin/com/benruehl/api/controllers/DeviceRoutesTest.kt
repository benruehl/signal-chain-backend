package com.benruehl.api.controllers

import com.benruehl.application.dtos.QueryDeviceResponse
import com.benruehl.application.dtos.SaveDeviceRequest
import com.benruehl.bodyDeserializedAs
import com.benruehl.domain.entities.Device
import com.benruehl.infrastructure.persistence.daos.DeviceDAO
import com.benruehl.setupApplication
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class DeviceRoutesTest {

    private val deviceRepository = DeviceDAO()

    @Test
    fun `GET all devices should return OK when no devices exist`() = setupApplication {
        client.get("/devices").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("[]", bodyAsText())
        }
    }

    @Test
    fun `GET all devices should return array when devices exist`() = setupApplication({
        deviceRepository.save(aDevice.copy(id = 1))
        deviceRepository.save(aDevice.copy(id = 2))
        deviceRepository.save(aDevice.copy(id = 3))
    }) {
        client.get("/devices").apply {
            val response = bodyDeserializedAs<List<QueryDeviceResponse>>()
            assertEquals(response.size, 3)
        }
    }

    @Test
    fun `GET device should return OK when device exists`() = setupApplication({
        deviceRepository.save(aDevice)
    }) {
        client.get("/devices/${aDevice.id}").apply {
            val response = bodyDeserializedAs<QueryDeviceResponse>()
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(aDevice.id, response.id)
            assertEquals(aDevice.title, response.title)
        }
    }

    @Test
    fun `GET device should return NotFound when device does not exist`() = setupApplication {
        client.get("/devices/123").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `POST device should return device with id`() = setupApplication { client ->
        client.post("/devices") {
            setBody(aDevice.toRequestBody())
            contentType(Json)
        }.apply {
            val response = bodyDeserializedAs<QueryDeviceResponse>()
            assertEquals(HttpStatusCode.Created, status)
            assertEquals(1, response.id)
        }
    }

    private val aDevice = Device(
        123,
        "Any Title",
        100f,
        100f
    )

    private fun Device.toRequestBody() = SaveDeviceRequest(
        title = this.title,
        positionX = this.positionX,
        positionY = this.positionY
    )
}