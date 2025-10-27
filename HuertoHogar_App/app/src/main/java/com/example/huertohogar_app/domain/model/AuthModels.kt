package com.example.huertohogar_app.domain.model

data class RegisterUser(
    val name: String,
    val email: String,
    val pass : String
)

data class LoggedUser(
    val id: Int,
    val name: String,
    val email: String
)