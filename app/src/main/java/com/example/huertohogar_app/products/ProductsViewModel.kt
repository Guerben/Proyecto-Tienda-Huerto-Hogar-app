package com.example.huertohogar_app.products
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar_app.data.ProductsRepositoryImpl
import com.example.huertohogar_app.domain.GetProductsUseCase
import com.example.huertohogar_app.domain.Product
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de la lista de productos.
 * Usa State para Compose.
 */
class ProductsViewModel : ViewModel() {

    // Inicialización manual para el ejemplo. En una app real se usaría Hilt/Koin.
    private val repository = ProductsRepositoryImpl()
    // FIX: Se elimina el prefijo de clase, se accede a 'repository' directamente.
    private val getProductsUseCase = GetProductsUseCase(repository)

    // El estado de la UI que será observado por Compose
    var uiState by mutableStateOf(ProductsUiState())
        private set

    init {
        // FIX: Se llama al método de instancia directamente.
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            // 1. Mostrar estado de carga
            // FIX: Se accede a 'uiState' directamente.
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                // 2. Ejecutar el caso de uso
                // FIX: Se accede a 'getProductsUseCase' directamente.
                val productosCargados = getProductsUseCase()

                // 3. Actualizar el estado con éxito
                // FIX: Se accede a 'uiState' directamente.
                uiState = uiState.copy(
                    productos = productosCargados,
                    isLoading = false
                )
            } catch (e: Exception) {
                // 4. Manejar errores
                // FIX: Se accede a 'uiState' directamente.
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Error al cargar: ${e.message}"
                )
            }
        }
    }
}

/**
 * Modelo de estado (State) para la UI de Compose.
 */
data class ProductsUiState(
    val productos: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
