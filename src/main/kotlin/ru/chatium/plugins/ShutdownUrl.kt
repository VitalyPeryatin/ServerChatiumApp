package ru.chatium.plugins

import io.ktor.server.application.*
import io.ktor.server.engine.*

fun Application.configureShutdownUrl() {
    install(ShutDownUrl.ApplicationCallPlugin) {
        shutDownUrl = "/shutdown"
        exitCodeSupplier = { 0 }
    }
}