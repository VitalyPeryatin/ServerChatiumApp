package ru.chatium

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

class AppDatabase(
    private val config: ApplicationConfig
) {

    lateinit var dataSource: DataSource

    fun init() {
        connectionPool()
        orm()
    }

    private fun connectionPool() {
        val dbConfig = config.config("ktor.database")
        val config = HikariConfig().apply {
            jdbcUrl = dbConfig.property("connection.jdbc").getString()
            username = dbConfig.property("connection.user").getString()
            password = dbConfig.property("connection.password").getString()
            isAutoCommit = false
            maximumPoolSize = 3
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        dataSource = HikariDataSource(config)
    }

    private fun orm() = Database.connect(dataSource)
}