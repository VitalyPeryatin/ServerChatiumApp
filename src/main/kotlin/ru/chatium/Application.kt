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
    val decodedBytes = Base64.getDecoder().decode("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")
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