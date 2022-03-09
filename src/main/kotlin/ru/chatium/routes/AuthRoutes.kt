package ru.chatium.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.chatium.di.DiContainer
import ru.chatium.models.User
import ru.chatium.models.SignUpCredentials
import java.io.File
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

fun Route.authRouting() {
    authenticate {
        get("/hello") {
            call.respondText("Hello, ${call.principal<JWTPrincipal>()?.payload?.getClaim("username")}!")
        }
    }

    post("/login") {
        val user = call.receive<User>()
        val token = generateJwkToken(user)
        call.respond(hashMapOf("token" to token))
    }
    post("/signup") {
        val credentials = call.receive<SignUpCredentials>()
        //val token = JwtConfig
    }
    static(".well-known") {
        staticRootFolder = File("certs")
        file("jwks.json")
    }
}

private fun generateJwkToken(user: User): String {
    val publicKey = DiContainer.jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
    val privateKeyString = DiContainer.configPrivateKeyString
    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
    val expiresAtMillis = Calendar.getInstance().apply { add(Calendar.WEEK_OF_MONTH, 2) }.time
    return JWT.create()
        .withAudience(DiContainer.configAudience)
        .withIssuer(DiContainer.configIssuer)
        .withClaim("username", user.username)
        .withExpiresAt(expiresAtMillis)
        .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
}
