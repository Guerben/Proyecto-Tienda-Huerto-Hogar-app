package com.example.huertohogar_app.auth
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    // 1. States locales para los campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // 2. Estado observable del ViewModel
    val state = viewModel.uiState

    // 3. Efecto para manejar la navegación tras un login exitoso
    LaunchedEffect(state.isUserLoggedIn) {
        if (state.isUserLoggedIn) {
            // --- INICIO DE LA CORRECCIÓN ---
            // Se invierte el orden para evitar una condición de carrera.
            // Primero navega, luego limpia el estado.
            onLoginSuccess()        // 1. Navega a la siguiente pantalla PRIMERO
            viewModel.clearStatus() // 2. Limpia el estado del ViewModel DESPUÉS
            // --- FIN DE LA CORRECCIÓN ---
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("HuertoHogar Login") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "¡Bienvenido!",
                style = MaterialTheme.typography.headlineLarge,
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                enabled = !state.isLoading
            )

            // Botón de Login (Email/Password)
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Iniciar sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Login con Google
            OutlinedButton(
                onClick = { viewModel.loginWithGoogle() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !state.isLoading
            ) {
                Text("Ingresar con Google")
            }

            // Mostrar Error
            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. BOTÓN PARA NAVEGAR AL REGISTRO
            TextButton(
                onClick = onNavigateToRegister,
                enabled = !state.isLoading
            ) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }
    }
}