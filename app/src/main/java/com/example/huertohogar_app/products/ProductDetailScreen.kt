package com.example.huertohogar_app.products
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Long,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Detalle del Producto #$productId") }) }
    ) { padding ->
        // Aquí iría el ViewModel para cargar los detalles del producto por ID
        Button(onClick = onNavigateBack, modifier = androidx.compose.ui.Modifier.padding(padding)) {
            Text("Volver al Catálogo")
        }
    }
}