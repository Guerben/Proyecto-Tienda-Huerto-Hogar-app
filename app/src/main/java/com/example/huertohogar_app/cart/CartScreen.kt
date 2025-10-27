package com.example.huertohogar_app.cart
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi Carrito 🛒") }) }
    ) { padding ->
        // Aquí iría la lista de ítems del carrito y el total.
        Button(onClick = onNavigateBack, modifier = androidx.compose.ui.Modifier.padding(padding)) {
            Text("Seguir Comprando")
        }
    }
}