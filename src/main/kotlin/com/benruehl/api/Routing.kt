package com.benruehl.api

import com.benruehl.api.controllers.deviceRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        deviceRouting()
    }
}
