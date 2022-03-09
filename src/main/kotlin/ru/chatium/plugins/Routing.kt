package ru.chatium.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import ru.chatium.routes.authRouting
import ru.chatium.routes.customerRouting

fun Application.configureRouting() {
    routing {
        customerRouting()
        authRouting()
    }
}
