package com.example.huertohogar_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

import com.example.huertohogar_app.core.AppNavGraph
import com.example.huertohogar_app.theme.HuertoHogar_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HuertoHogar_appTheme {
                // Usamos Surface como contenedor principal.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamamos a la función AppNavGraph que contiene el NavHost, el NavController
                    // y todas las rutas definidas en tu archivo NavGraph.kt.
                    AppNavGraph()
                }
            }
        }

    }
}
// NOTA: Se han eliminado los composables de ejemplo (LoginScreen, ProductosScreen)
// que tenías aquí, ya que tu NavGraph apunta a versiones en los paquetes auth y products.