package ru.chatium.models

import kotlinx.serialization.Serializable

@Serializable
data class SignUpCredentials(
    val login: String,
    val password: String
)