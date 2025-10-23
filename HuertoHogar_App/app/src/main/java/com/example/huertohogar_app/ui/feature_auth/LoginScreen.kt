package com.example.huertohogar_app.ui.feature_auth

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onRegisterClick: () -> Unit // Función para navegar a la pantalla de registro
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = viewModel.email, onValueChange = viewModel::onEmailChange, label = { Text("Email") })
        OutlinedTextField(value = viewModel.password, onValueChange = viewModel::onPasswordChange, label = { Text("Contraseña") })
        Button(onClick = viewModel::onLoginClick) {
            Text("Iniciar Sesión")
        }
        TextButton(onClick = onRegisterClick) {
            Text("¿No tienes cuenta? Regístrate")
        }

        // Muestra el mensaje del ViewModel si no es nulo
        viewModel.authMessage?.let { message ->
            Text(message, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }
    }
}