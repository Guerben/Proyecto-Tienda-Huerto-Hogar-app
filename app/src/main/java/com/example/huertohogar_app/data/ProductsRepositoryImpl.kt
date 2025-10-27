package com.example.huertohogar_app.data
import com.example.huertohogar_app.domain.ProductsRepository
import com.example.huertohogar_app.domain.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ProductsRepositoryImpl : ProductsRepository {

    private val fakeProducts = listOf(
        Product(1, "Leche orgánica", "Leche de vacas libres de pastoreo", 1.50, "", 20),
        Product(2, "Manzanas Rojas", "Cosecha fresca, variedad Fuji", 0.80, "", 50),
        Product(3, "Yogur Natural", "Elaborado con fermentos vivos", 2.20, "", 15),
        Product(4, "Zanahorias", "Orgánicas y crujientes", 1.10, "", 30)
    )

    override suspend fun obtenerProductos(): List<Product> = withContext(Dispatchers.IO) {
        // Simulación de una llamada de red o BBDD lenta
        delay(1000)
        return@withContext fakeProducts
    }

    override suspend fun obtenerProductoPorId(id: Long): Product? = withContext(Dispatchers.IO) {
        // Simulación de una llamada de red o BBDD
        delay(500)
        var result: Product? = null
        // Usamos for/if en lugar de find para alinearnos con el código simple
        for (p in fakeProducts) {
            if (p.id == id) {
                result = p
                break
            }
        }
        return@withContext result
    }
}