package ru.chatium.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.chatium.data.network.models.UserPrincipal
import ru.chatium.data.repository.UsersRepository

val usersRepository = UsersRepository()

fun Route.authRouting() {

    authenticate {
        get("/hello") {
            call.respondText("Hello, ${call.principal<UserPrincipal>()?.id}!")
        }
        post("/login") {
            val user = call.principal<UserPrincipal>()
            if (user == null) {
                call.respond("Error with getting UserPrincipal")
                return@post
            }
            if (!usersRepository.hasUser(userId = user.id)) {
                usersRepository.addNewUser(user)
            }
            call.respond(Unit)
        }
    }

    get("/test") {
        call.respondText("123")
    }
}