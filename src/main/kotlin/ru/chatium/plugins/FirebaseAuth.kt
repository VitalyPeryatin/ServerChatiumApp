package ru.chatium.plugins

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory


private val firebaseAuthLogger: Logger = LoggerFactory.getLogger("io.robothouse.auth.firebase")

class FirebaseAuthenticationProvider(config: Configuration) : AuthenticationProvider(config) {

    val token: (ApplicationCall) -> String? = config.token
    val principal: ((uid: String, phoneNumber: String) -> Principal?)? = config.principal

    class Configuration(name: String?) : AuthenticationProvider.Configuration(name) {

        var token: (ApplicationCall) -> String? = { call -> call.request.parseAuthorizationToken() }
        var principal: ((uid: String, phoneNumber: String) -> Principal?)? = null

        fun build() = FirebaseAuthenticationProvider(this)
    }
}

fun Authentication.Configuration.firebase(
    name: String? = null,
    configure: FirebaseAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = FirebaseAuthenticationProvider.Configuration(name).apply(configure).build()
    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->

        try {
            val token = provider.token(call) ?: throw FirebaseAuthException(
                FirebaseException(
                    ErrorCode.UNAUTHENTICATED,
                    "No token could be found",
                    null
                )
            )

            val firebaseAuth = FirebaseAuth.getInstance()
            val verifiedToken = firebaseAuth.verifyIdToken(token)
            val uid = verifiedToken.uid
            val phoneNumber = firebaseAuth.getUser(uid).phoneNumber

            provider.principal?.invoke(uid, phoneNumber)?.let { principal ->
                context.principal(principal)
            }

        } catch (cause: Throwable) {
            val message = if (cause is FirebaseAuthException) {
                "Authentication failed: ${cause.message ?: cause.javaClass.simpleName}"
            } else {
                cause.message ?: cause.javaClass.simpleName
            }
            firebaseAuthLogger.trace(message)
            call.respond(HttpStatusCode.Unauthorized, message)
            context.challenge.complete()
            finish()
        }
    }
    register(provider)
}

fun ApplicationRequest.parseAuthorizationToken(): String? = authorization()?.let {
    it.split(" ")[1]
}