package com.example.huertohogar_app.auth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel para la autenticación (Login y Registro).
 * Simula el uso de una librería externa como Firebase Auth.
 */
class AuthViewModel : ViewModel() {

    // Estado observable para la UI
    var uiState by mutableStateOf(AuthUiState())
        private set


    /**
     * Simulación de una base de datos de usuarios (Email -> Password).

     */
    private val userDatabase = mutableMapOf(
        "test@test.com" to "123456"
    )



    /**
     * Simula la operación de inicio de sesión con Email y Contraseña.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            delay(1500) // Simulación de una llamada a Firebase Auth


            // 2. Comprueba si el email existe en nuestro mapa Y si la contraseña coincide
            val storedPassword = userDatabase[email]
            if (storedPassword != null && storedPassword == password) {
                // Éxito
                uiState = uiState.copy(
                    isLoading = false,
                    isUserLoggedIn = true
                )
            } else {
                // Fallo: error de credenciales
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Credenciales incorrectas." // Ahora sí, el error es real
                )
            }
            // --- FIN DE LA CORRECCIÓN ---
        }
    }

    /**
     * Simula la operación de registro de un nuevo usuario.
     */
    fun register(email: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            delay(2000) // Simulación de registro


            // 3. Comprueba si el email ya existe en nuestro mapa
            if (userDatabase.containsKey(email)) {
                // Simulación de un error de registro (email ya en uso)
                uiState = uiState.copy(
                    isLoading = false,
                    error = "El email ya está en uso. (Simulación)"
                )
            } else {
                // Éxito: el usuario se registra en nuestro mapa
                userDatabase[email] = password

                // Ponemos isUserLoggedIn = true para que el LaunchedEffect
                // de RegisterScreen reaccione y navegue al Login.
                uiState = uiState.copy(
                    isLoading = false,
                    isUserLoggedIn = true
                )
            }

        }
    }

    /**
     * Simula el inicio de sesión con Google.
     */
    fun loginWithGoogle() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            delay(1000) // Simulación

            // Éxito simulado
            uiState = uiState.copy(
                isLoading = false,
                isUserLoggedIn = true
            )
        }
    }

    /**
     * Reinicia el estado después de un evento (ej. navegación)
     */
    fun clearStatus() {
        uiState = uiState.copy(isUserLoggedIn = false, error = null)
    }
}



/**
 * Modelo de estado para la UI de Autenticación.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val error: String? = null
)