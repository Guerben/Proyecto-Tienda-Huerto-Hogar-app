package com.example.huertohogar_app.products

// --- IMPORTS AÑADIDOS PARA UBICACIÓN Y EL ICONO ---
import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
// --- FIN DE IMPORTS AÑADIDOS ---

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogar_app.domain.Product


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onOpenCart: () -> Unit,
    onProductSelected: (Long) -> Unit,
    onLogout: () -> Unit,
    viewModel: ProductsViewModel = viewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.cargarProductos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo HuertoHogar") },
                actions = {
                    // --- ERROR CORREGIDO ---
                    // El botón del carrito
                    Button(onClick = onOpenCart, modifier = Modifier.padding(end = 8.dp)) {
                        Text("🛒 Carrito")
                    }
                    // El botón de logout (estaba anidado incorrectamente)
                    IconButton(onClick = onLogout) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión")
                    }
                    // --- FIN DE LA CORRECCIÓN ---
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            // --- COMPONENTE DE UBICACIÓN AÑADIDO ---
            LocationComponent()
            HorizontalDivider() // Un separador
            // --- FIN DEL COMPONENTE DE UBICACIÓN ---

            when {
                state.isLoading -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Cargando productos...", style = MaterialTheme.typography.titleMedium)
                    }
                }
                state.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "ERROR: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(32.dp)
                        )
                    }
                }
                else -> {
                    LazyColumn {
                        items(state.productos) { product ->
                            ProductItem(
                                product = product,
                                onClick = { onProductSelected(product.id) }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Componente individual para cada producto en la lista.
 */
@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    // ... (Tu código de ProductItem está perfecto, no se necesita cambiar)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("📷")
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = product.descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }

            Text(
                text = "$${String.format("%.2f", product.precio)}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


/**
 * Composable privado para gestionar la lógica de permisos y obtención de ubicación.
 */
@SuppressLint("MissingPermission") // Suprimimos el warning porque Accompanist gestiona el permiso
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationComponent() {
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("Buscando ubicación...") }

    // 1. Define los permisos necesarios
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    // 2. Comprueba si todos los permisos fueron concedidos
    if (locationPermissionsState.allPermissionsGranted) {
        // --- SÍ TENEMOS PERMISOS ---
        // Usamos LaunchedEffect para obtener la ubicación solo una vez
        LaunchedEffect(Unit) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    locationText = if (location != null) {
                        "Ubicación: Lat ${location.latitude}, Lon ${location.longitude}"
                    } else {
                        "No se pudo obtener la ubicación. (¿GPS activado?)"
                    }
                }
                .addOnFailureListener {
                    locationText = "Error al obtener ubicación."
                }
        }

        // Muestra la ubicación
        Text(
            text = locationText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        )

    } else {
        // --- NO TENEMOS PERMISOS ---
        // Muestra un texto y un botón para solicitarlos
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "La app necesita tu ubicación para mostrar productos cercanos.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                Text("Activar Ubicación")
            }
        }
    }
}