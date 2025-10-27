package com.example.huertohogar_app.domain

class GetProductsUseCase (
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(): List<Product> {
        return repository.obtenerProductos()
    }
}