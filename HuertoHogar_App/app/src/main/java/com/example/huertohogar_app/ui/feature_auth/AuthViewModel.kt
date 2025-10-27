package com.example.huertohogar_app.ui.feature_auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar_app.domain.model.RegisterUser
import com.example.huertohogar_app.domain.use_case.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var authMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onNameChange(newName: String) {
        name = newName
        clearMessage()
    }

    fun onEmailChange(newEmail: String) {
        email = newEmail
        clearMessage()
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        clearMessage()
    }

    fun onRegisterClick(onSuccess: () -> Unit = {}) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            authMessage = "Por favor, completa todos los campos"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val user = RegisterUser(name, email, password)
                val result = repository.register(user)
                if (result.isSuccess) {
                    authMessage = "Registro exitoso"
                    onSuccess()
                } else {
                    authMessage = result.exceptionOrNull()?.message ?: "Error en el registro"
                }
            } catch (e: Exception) {
                authMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun onLoginClick(onSuccess: () -> Unit = {}) {
        if (email.isEmpty() || password.isEmpty()) {
            authMessage = "Por favor, completa todos los campos"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                if (result.isSuccess) {
                    val loggedUser = result.getOrNull()
                    authMessage = "Bienvenido, ${loggedUser?.name ?: "Usuario"}"
                    onSuccess()
                } else {
                    authMessage = result.exceptionOrNull()?.message ?: "Error en el login"
                }
            } catch (e: Exception) {
                authMessage = "Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    private fun clearMessage() {
        authMessage = null
    }
}