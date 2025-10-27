package com.example.huertohogar_app.ui.feature_auth

@Composable
fun RegisterScreen(viewModel: AuthViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = viewModel.name, onValueChange = viewModel::onNameChange, label = { Text("Nombre") })
        OutlinedTextField(value = viewModel.email, onValueChange = viewModel::onEmailChange, label = { Text("Email") })
        OutlinedTextField(value = viewModel.password, onValueChange = viewModel::onPasswordChange, label = { Text("Contraseña") })
        Button(onClick = viewModel::onRegisterClick) {
            Text("Registrarse")
            }
        }
}