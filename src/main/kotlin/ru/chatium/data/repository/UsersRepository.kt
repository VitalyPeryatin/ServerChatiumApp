package ru.chatium.data.repository

import ru.chatium.data.db.dao.UsersDao
import ru.chatium.data.network.models.UserResponse

class UsersRepository {

    private val usersDao = UsersDao()

    suspend fun addNewUser(user: UserResponse): Boolean {
        return usersDao.addNewUser(user)
    }

    suspend fun getAllUsers(): List<UserResponse> {
        return usersDao.getAllUsers()
    }

    suspend fun deleteUser(id: String): Boolean {
        return usersDao.deleteUser(id)
    }

    suspend fun isValidUserCredentials(user: UserResponse): Boolean {
        return usersDao.isValidUserCredentials(user)
    }

    suspend fun hasUserWithLogin(login: String): Boolean {
        return usersDao.hasUserWithLogin(login)
    }
}