package ru.chatium

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import org.slf4j.LoggerFactory
import ru.chatium.data.db.DatabaseFactory
import ru.chatium.di.DiContainer
import ru.chatium.plugins.*
import ru.chatium.routes.authRouting
import ru.chatium.routes.customerRouting
import ru.chatium.routes.usersRouting
import java.io.File
import java.util.*

val logger = LoggerFactory.getLogger("MY_LOGGER")

fun main(args: Array<String>) {
    logger.debug("APP_DEBUG: 1.0")
    generateJksFile()
    logger.debug("APP_DEBUG: 2.0")
    EngineMain.main(args)
}

private fun generateJksFile() {
    logger.debug("APP_DEBUG: 1.1")
    val base64 = System.getenv("BASE64")
    logger.debug("APP_DEBUG: 1.2")
    val decodedBytes = Base64.getDecoder().decode(base64)
    logger.debug("APP_DEBUG: 1.3")
    File("keystore.jks").writeBytes(decodedBytes)
    logger.debug("APP_DEBUG: 1.4")
}

fun Application.allModules() {
    logger.debug("APP_DEBUG: 2.1")
    DiContainer.application = this
    DatabaseFactory.init(environment.config)
    logger.debug("APP_DEBUG: 2.2")

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