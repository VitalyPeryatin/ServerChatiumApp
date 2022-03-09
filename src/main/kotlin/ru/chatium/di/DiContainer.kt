package ru.chatium.di

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import io.ktor.server.application.*
import java.util.concurrent.TimeUnit

object DiContainer {

    lateinit var application: Application

    val jwkProvider: JwkProvider by lazy {
        JwkProviderBuilder(configIssuer)
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    }

    val configIssuer: String by lazy {
        application.environment.config.property("jwt.issuer").getString()
    }

    val configAudience: String by lazy {
        application.environment.config.property("jwt.audience").getString()
    }

    val configPrivateKeyString: String by lazy {
        application.environment.config.property("jwt.privateKey").getString()
    }

    val configRealm: String by lazy {
        application.environment.config.property("jwt.realm").getString()
    }
}