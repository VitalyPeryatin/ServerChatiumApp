package ru.chatium.data.db.models

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object UsersTable: IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("id", length = 128).entityId()
    val username = varchar("username", length = 64).nullable()
    val phoneNumber = varchar("phone_number", length = 64).uniqueIndex()
}