package ru.chatium.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.chatium.data.network.models.UserPrincipal
import ru.chatium.data.network.models.UserResponse
import ru.chatium.data.repository.UsersRepository

fun Route.adminRouting() {

    val usersRepository = UsersRepository()

    route("admin/users") {
        get {
            val users = usersRepository.getAllUsers()
            if (users.isEmpty()) {
                call.respondText("No users")
            } else {
                call.respond(users)
            }
        }
        post("admin/users") {
            val user = call.receive<UserResponse>()
            val userPrincipal = UserPrincipal(id = user.id, phoneNumber = user.phoneNumber)
            val isUserAdded = usersRepository.addNewUser(userPrincipal)
            call.respondText("UserAdded: $isUserAdded")
        }
        delete("admin/users/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val isUserDeleted = usersRepository.deleteUser(id)
            call.respondText("UserDeleted: $isUserDeleted")
        }
    }
}