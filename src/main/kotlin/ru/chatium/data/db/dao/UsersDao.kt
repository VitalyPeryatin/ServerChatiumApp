package ru.chatium.data.db.dao

import org.jetbrains.exposed.sql.*
import ru.chatium.data.db.DatabaseFactory
import ru.chatium.data.db.models.UsersTable
import ru.chatium.data.network.models.UserPrincipal
import ru.chatium.data.network.models.UserResponse

class UsersDao {

    private fun resultRowToUser(row: ResultRow): UserResponse {
        return UserResponse(
            id = row[UsersTable.id].toString(),
            username = row[UsersTable.username],
            phoneNumber = row[UsersTable.phoneNumber],
        )
    }

    suspend fun addNewUser(user: UserPrincipal): Boolean {
        return DatabaseFactory.dbQuery {
            val insertStatement = UsersTable.insert {
                it[id] = user.id
                it[username] = null
                it[phoneNumber] = user.phoneNumber
            }
            insertStatement.resultedValues?.singleOrNull() != null
        }
    }

    suspend fun getAllUsers(): List<UserResponse> {
        return DatabaseFactory.dbQuery {
            UsersTable.selectAll().map(::resultRowToUser)
        }
    }

    suspend fun deleteUser(id: String): Boolean {
        return DatabaseFactory.dbQuery {
            UsersTable.deleteWhere { UsersTable.id eq id } > 0
        }
    }

    suspend fun hasUser(userId: String): Boolean {
        return DatabaseFactory.dbQuery {
            UsersTable.select { UsersTable.id eq userId }.any()
        }
    }
}