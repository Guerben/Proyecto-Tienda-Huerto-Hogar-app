package com.example.huertohogar_app.domain

data class Product(
        val id: Long,
        val nombre: String,
        val descripcion: String,
        val precio: Double,
        val imagenUrl: String,
        val stock: Int
    )
