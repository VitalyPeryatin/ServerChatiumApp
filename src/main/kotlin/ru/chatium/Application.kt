package ru.chatium

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import ru.chatium.data.db.DatabaseFactory
import ru.chatium.di.DiContainer
import ru.chatium.plugins.*
import ru.chatium.routes.authRouting
import ru.chatium.routes.customerRouting
import ru.chatium.routes.adminRouting
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    generateJksFile()
    EngineMain.main(args)
}

private fun generateJksFile() {
    val base64 = System.getenv("BASE64")
    val decodedBytes = Base64.getDecoder().decode(base64)
    File("keystore.jks").writeBytes(decodedBytes)
}

fun Application.allModules() {
    DiContainer.application = this
    DatabaseFactory.init(environment.config)

    configureShutdownUrl()
    configureAuthentication()
    configureSockets()
    configureSerialization()

    routing {
        customerRouting()
        authRouting()
        adminRouting()
    }
}