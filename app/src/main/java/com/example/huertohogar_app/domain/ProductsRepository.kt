package com.example.huertohogar_app.domain

interface ProductsRepository {
    suspend fun obtenerProductos(): List<Product>
    suspend fun obtenerProductoPorId(id: Long): Product?
}