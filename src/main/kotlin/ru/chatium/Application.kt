package ru.chatium

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import ru.chatium.data.db.DatabaseFactory
import ru.chatium.di.DiContainer
import ru.chatium.plugins.*
import ru.chatium.routes.authRouting
import ru.chatium.routes.customerRouting
import ru.chatium.routes.usersRouting
import java.io.File
import java.util.*


fun main(args: Array<String>) {
    println("APP_DEBUG: 1.0")
    generateJksFile()
    println("APP_DEBUG: 2.0")
    EngineMain.main(args)
}

private fun generateJksFile() {
    println("APP_DEBUG: 1.1")
    val base64 = System.getenv("BASE64")
    println("APP_DEBUG: 1.2")
    val decodedBytes = Base64.getDecoder().decode(base64)
    println("APP_DEBUG: 1.3")
    File("keystore.jks").writeBytes(decodedBytes)
    println("APP_DEBUG: 1.4")
}

fun Application.allModules() {
    println("APP_DEBUG: 2.1")
    DiContainer.application = this
    DatabaseFactory.init(environment.config)
    println("APP_DEBUG: 2.2")

    configureShutdownUrl()
    configureAuthentication()
    configureSockets()
    configureSerialization()

    routing {
        customerRouting()
        authRouting()
        usersRouting()
    }
}