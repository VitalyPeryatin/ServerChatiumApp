package ru.chatium.data.db.models

import org.jetbrains.exposed.dao.id.UUIDTable

object UsersTable: UUIDTable() {
    val login = varchar("title", 128).uniqueIndex()
    val password = varchar("body", 1024)
}