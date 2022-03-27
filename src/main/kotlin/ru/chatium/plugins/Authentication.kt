package ru.chatium.plugins

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.*
import io.ktor.server.auth.*
import ru.chatium.data.network.models.UserPrincipal

fun Application.configureAuthentication() {
    initFirebaseApp()
    install(Authentication) {
        firebase { principal = ::UserPrincipal }
    }
}

private fun Application.initFirebaseApp() {
    val firebaseAdminCredentialsPath = "chatium-ee362-firebase-adminsdk-yx443-fafbbf5cf8.json"
    val serviceAccount = this::class.java.classLoader.getResourceAsStream(firebaseAdminCredentialsPath)
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    FirebaseApp.initializeApp(options)
}