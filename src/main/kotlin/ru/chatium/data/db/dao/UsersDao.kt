package ru.chatium.data.db.dao

import org.jetbrains.exposed.sql.*
import ru.chatium.data.db.DatabaseFactory
import ru.chatium.data.db.models.UsersTable
import ru.chatium.data.network.models.UserResponse
import java.util.*

class UsersDao {

    private fun resultRowToUser(row: ResultRow): UserResponse {
        return UserResponse(
            id = row[UsersTable.id].toString(),
            login = row[UsersTable.login],
            password = row[UsersTable.password],
        )
    }

    suspend fun addNewUser(user: UserResponse): Boolean {
        return DatabaseFactory.dbQuery {
            val insertStatement = UsersTable.insert {
                it[login] = user.login
                it[password] = user.password
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
            UsersTable.deleteWhere { UsersTable.id eq UUID.fromString(id) } > 0
        }
    }

    suspend fun isValidUserCredentials(user: UserResponse): Boolean {
        return DatabaseFactory.dbQuery {
            UsersTable.select {
                (UsersTable.login eq user.login) and (UsersTable.password eq user.password)
            }.any()
        }
    }

    suspend fun hasUserWithLogin(login: String): Boolean {
        return DatabaseFactory.dbQuery {
            UsersTable.select { UsersTable.login eq login }.any()
        }
    }
}