package ru.chatium.data.network.models

import io.ktor.server.auth.*

data class UserPrincipal(
    val id: String,
    val phoneNumber: String
): Principal