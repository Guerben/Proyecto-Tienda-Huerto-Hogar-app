package com.example.huertohogar_app.domain.use_case.repository

import com.example.huertohogar_app.domain.model.LoggedUser
import com.example.huertohogar_app.domain.model.RegisterUser

interface AuthRepository {

    suspend fun register(user: RegisterUser): Result<Unit>
    suspend fun login(email: String, pass: String): Result<LoggedUser>
}
