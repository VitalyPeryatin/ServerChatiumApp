package ru.chatium.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import ru.chatium.data.network.models.UserResponse
import ru.chatium.data.repository.UsersRepository
import ru.chatium.di.DiContainer
import java.io.File
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

val usersRepository = UsersRepository()

fun Route.authRouting() {

    authenticate {
        get("/hello") {
            call.respondText("Hello, ${call.principal<JWTPrincipal>()?.payload?.getClaim("username")}!")
        }
    }

    get("/test") {
        call.respondText("123")
    }
    post("/login") {
        val user = call.receive<UserResponse>()
        login(user)
    }
    post("/signup") {
        val user = call.receive<UserResponse>()

        if (usersRepository.hasUserWithLogin(user.login)) {
            call.respondText("User with login '${user.login}' already exists")
            return@post
        }

        if (user.login.trim().length < 3 || user.password.trim().length < 3) {
            call.respondText("Too short login or password")
            return@post
        }

        val isAddedUser = usersRepository.addNewUser(user)
        if (!isAddedUser) {
            call.respondText("Error while adding new user")
            return@post
        }

        login(user)
    }
    static("/.well-known") {
        staticRootFolder = File("/app/src/main/resources/certs")
        file("jwks.json")
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.login(user: UserResponse) {
    if (usersRepository.isValidUserCredentials(user)) {
        val token = generateJwkToken(user)
        call.respond(hashMapOf("token" to token))
    } else {
        call.respond(HttpStatusCode.Unauthorized)
    }
}

private fun generateJwkToken(user: UserResponse): String {
    val publicKey = DiContainer.jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey
    val privateKeyString = DiContainer.configPrivateKeyString
    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
    val expiresAtMillis = Calendar.getInstance().apply { add(Calendar.WEEK_OF_MONTH, 2) }.time
    return JWT.create()
        .withAudience(DiContainer.configAudience)
        .withIssuer(DiContainer.configIssuer)
        .withClaim("username", user.login)
        .withExpiresAt(expiresAtMillis)
        .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
}
