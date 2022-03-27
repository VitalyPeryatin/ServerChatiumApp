package ru.chatium.data.network.models

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: String,
    val phoneNumber: String,
    val username: String? = null
)