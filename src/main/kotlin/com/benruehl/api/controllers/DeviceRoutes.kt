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
            val id: Int
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
            deviceService.create(device)
            call.respond(Created, device)
        }

        put("{id?}") {
            val id: Int
            try {
                id = call.getIdParameter()
            } catch (e: IllegalArgumentException) {
                return@put call.respondText(
                    text = e.message ?: "",
                    status = BadRequest
                )
            }
            val device = call.receive<SaveDeviceRequest>()
            deviceService.update(id, device)
            call.respond(device)
        }

        delete("{id?}") {
            val id: Int
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

private fun ApplicationCall.getIdParameter(): Int {
    val idAsString = this.parameters["id"] ?: throw IllegalArgumentException("Missing id")
    val id = idAsString.toIntOrNull() ?: throw IllegalArgumentException("Id is not an integer")
    return id
}