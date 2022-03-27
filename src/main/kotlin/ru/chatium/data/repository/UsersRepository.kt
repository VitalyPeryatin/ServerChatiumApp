package ru.chatium.data.repository

import ru.chatium.data.db.dao.UsersDao
import ru.chatium.data.network.models.UserPrincipal
import ru.chatium.data.network.models.UserResponse

class UsersRepository {

    private val usersDao = UsersDao()

    suspend fun addNewUser(user: UserPrincipal): Boolean {
        return usersDao.addNewUser(user)
    }

    suspend fun getAllUsers(): List<UserResponse> {
        return usersDao.getAllUsers()
    }

    suspend fun deleteUser(id: String): Boolean {
        return usersDao.deleteUser(id)
    }

    suspend fun hasUser(userId: String): Boolean {
        return usersDao.hasUser(userId)
    }
}