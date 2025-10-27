package com.example.huertohogar_app.auth
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api // <-- Para TopAppBar (si es necesario)
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.* // Contiene Composable, remember, getValue/setValue, LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class) // Solución al error experimental en TopAppBar
@Composable
fun RegisterScreen(
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    // 1. Estados locales para los campos del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // 2. Estado observable del ViewModel (para carga y errores)
    val state = viewModel.uiState

    // 3. Efecto para manejar la navegación tras un registro exitoso (simulado)
    LaunchedEffect(state.isUserLoggedIn) {
        if (state.isUserLoggedIn) {
            onRegistrationSuccess() // Navega tras el registro
            viewModel.clearStatus()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Registro HuertoHogar") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // FIX: Usar el estilo de tipografía de Material 3 (headlineLarge)
            Text(
                "Crea tu cuenta",
                style = MaterialTheme.typography.headlineLarge, // Corregido de .h4 a .headlineLarge
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                enabled = !state.isLoading
            )

            // Campo de Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                enabled = !state.isLoading
            )

            // Campo de Confirmación de Contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                enabled = !state.isLoading
            )

            // Botón de Registro
            Button(
                onClick = {
                    if (password == confirmPassword) {
                        viewModel.register(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !state.isLoading && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
            ) {
                if (state.isLoading) {
                    // FIX: En M3, el color se infiere, o usa colorScheme.onPrimary si necesitas forzarlo.
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Registrarse")
                }
            }

            // Mostrar Error
            state.error?.let {
                Text(
                    text = it,
                    // FIX: Usar el esquema de color M3 (colorScheme.error)
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Opción para ir al Login
            TextButton(onClick = onNavigateToLogin, enabled = !state.isLoading) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}