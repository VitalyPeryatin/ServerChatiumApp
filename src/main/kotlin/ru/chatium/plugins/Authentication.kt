package ru.chatium.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.chatium.data.repository.UsersRepository
import ru.chatium.di.DiContainer

fun Application.configureAuthentication() {

    val usersRepository = UsersRepository()

    val jwkProvider = DiContainer.jwkProvider
    install(Authentication) {
        jwt {
            realm = DiContainer.configRealm
            verifier(jwkProvider, DiContainer.configIssuer) {
                acceptLeeway(3)
            }
            validate { credential ->
                val login = credential.payload.getClaim("username").asString()
                if (usersRepository.hasUserWithLogin(login)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}