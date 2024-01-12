package com.benruehl.api.controllers

import com.benruehl.application.dtos.SaveDeviceRequest
import com.benruehl.application.services.DeviceService
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deviceRouting() {
    val deviceService = DeviceService()

    route("/devices") {
        get {
            call.respond(deviceService.findAll())
        }

        get("{id?}") {
            val id: String
            try {
                id = call.getIdParameter()
            } catch (e: IllegalArgumentException) {
                return@get call.respondText(
                    text = e.message ?: "",
                    status = BadRequest
                )
            }
            val device =
                deviceService.find(id) ?: return@get call.respondText(
                    "No device with id $id",
                    status = NotFound
                )
            call.respond(device)
        }

        post {
            val device = call.receive<SaveDeviceRequest>()
            val createdDevice = deviceService.create(device)
            call.respond(Created, createdDevice)
        }

        put("{id?}") {
            val id: String
            try {
                id = call.getIdParameter()
            } catch (e: IllegalArgumentException) {
                return@put call.respondText(
                    text = e.message ?: "",
                    status = BadRequest
                )
            }
            val device = call.receive<SaveDeviceRequest>()
            val updatedDevice = deviceService.update(id, device)
            call.respond(updatedDevice)
        }

        delete("{id?}") {
            val id: String
            try {
                id = call.getIdParameter()
            } catch (e: IllegalArgumentException) {
                return@delete call.respondText(
                    text = e.message ?: "",
                    status = BadRequest
                )
            }
            if (deviceService.delete(id)) {
                call.respond(OK)
            } else {
                call.respond(NotFound)
            }
        }
    }
}

private fun ApplicationCall.getIdParameter() =
    this.parameters["id"] ?: throw IllegalArgumentException("Missing id")
