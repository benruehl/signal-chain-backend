package com.benruehl.api.controllers

import com.benruehl.application.dtos.QueryDeviceResponse
import com.benruehl.bodyDeserializedAs
import com.benruehl.domain.entities.Device
import com.benruehl.infrastructure.persistence.daos.DeviceDAO
import com.benruehl.setupApplication
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
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

    private val aDevice = Device(
        123,
        "Any Title",
        100f,
        100f
    )
}