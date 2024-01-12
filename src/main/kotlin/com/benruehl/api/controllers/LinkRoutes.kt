package com.benruehl.api.controllers

import com.benruehl.application.dtos.CreateLinkRequest
import com.benruehl.application.services.LinkService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.linkRouting() {
    val linkService = LinkService()

    route("/links") {
        post {
            val link = call.receive<CreateLinkRequest>()
            val createdLink = linkService.create(link)
            call.respond(HttpStatusCode.Created, createdLink)
        }

        delete("{id?}") {
            val id: String
            try {
                id = call.getIdParameter()
            } catch (e: IllegalArgumentException) {
                return@delete call.respondText(
                    text = e.message ?: "",
                    status = HttpStatusCode.BadRequest
                )
            }
            if (linkService.delete(id)) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

private fun ApplicationCall.getIdParameter() =
    this.parameters["id"] ?: throw IllegalArgumentException("Missing id")