package com.benruehl.api

import com.benruehl.api.controllers.deviceRouting
import com.benruehl.api.controllers.linkRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        deviceRouting()
        linkRouting()
    }
}
