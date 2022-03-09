package ru.chatium.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.chatium.di.DiContainer

fun Application.configureAuthentication() {
    val jwkProvider = DiContainer.jwkProvider
    install(Authentication) {
        jwt {
            realm = DiContainer.configRealm
            verifier(jwkProvider, DiContainer.configIssuer) {
                acceptLeeway(3)
            }
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}