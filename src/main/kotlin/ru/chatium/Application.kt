package ru.chatium

import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.chatium.di.DiContainer
import ru.chatium.plugins.*
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val base64 = System.getenv("BASE64")
    val decodedBytes = Base64.getDecoder().decode(base64)
    File("keystore.jks").writeBytes(decodedBytes)
    EngineMain.main(args)
}

fun Application.allModules() {

    DiContainer.application = this

    configureShutdownUrl()
    configureAuthentication()
    configureSockets()
    configureRouting()
    configureSerialization()

    /*val database = AppDatabase(environment.config)
    database.init()

    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(Cities)

        val stPeteId = Cities.insert {
            it[name] = "St. Petersburg"
        } get Cities.id

        println("Cities: ${Cities.selectAll()}")
    }*/
}

object Cities: IntIdTable() {
    val name = varchar("name", 50)
}