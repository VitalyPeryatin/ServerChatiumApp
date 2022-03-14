package ru.chatium.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.chatium.data.network.models.UserResponse
import ru.chatium.data.repository.UsersRepository

fun Route.usersRouting() {

    val usersRepository = UsersRepository()

    route("/users") {
        get {
            val users = usersRepository.getAllUsers()
            if (users.isEmpty()) {
                call.respondText("No users")
            } else {
                call.respond(users)
            }
        }
        post {
            val user = call.receive<UserResponse>()
            val isUserAdded = usersRepository.addNewUser(user)
            call.respondText("UserAdded: $isUserAdded")
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val isUserDeleted = usersRepository.deleteUser(id)
            call.respondText("UserDeleted: $isUserDeleted")
        }
    }
}