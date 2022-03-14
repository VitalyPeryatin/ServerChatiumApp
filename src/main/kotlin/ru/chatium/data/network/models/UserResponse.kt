package ru.chatium.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String? = null,
    val login: String,
    val password: String
)